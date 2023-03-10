package dev.su5ed.gtexperimental.blockentity.base;

import com.mojang.authlib.GameProfile;
import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.api.cover.Cover;
import dev.su5ed.gtexperimental.api.cover.CoverCategory;
import dev.su5ed.gtexperimental.api.cover.CoverHandler;
import dev.su5ed.gtexperimental.api.cover.CoverInteractionResult;
import dev.su5ed.gtexperimental.api.cover.CoverType;
import dev.su5ed.gtexperimental.api.machine.MachineController;
import dev.su5ed.gtexperimental.api.machine.ScannerInfoProvider;
import dev.su5ed.gtexperimental.api.util.FriendlyCompoundTag;
import dev.su5ed.gtexperimental.blockentity.component.CoverHandlerImpl;
import dev.su5ed.gtexperimental.blockentity.component.MachineControllerImpl;
import dev.su5ed.gtexperimental.cover.GenericCover;
import dev.su5ed.gtexperimental.cover.VentCover;
import dev.su5ed.gtexperimental.network.GregTechNetwork;
import dev.su5ed.gtexperimental.network.Networked;
import dev.su5ed.gtexperimental.object.ModCovers;
import dev.su5ed.gtexperimental.util.BlockEntityProvider;
import dev.su5ed.gtexperimental.util.GtLocale;
import dev.su5ed.gtexperimental.util.GtUtil;
import dev.su5ed.gtexperimental.util.inventory.MachineSlotItemHandler;
import dev.su5ed.gtexperimental.util.inventory.SlotAwareItemHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.model.data.ModelData;
import one.util.streamex.StreamEx;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CoverableBlockEntity extends InventoryBlockEntity implements ScannerInfoProvider {
    @Networked
    private final CoverHandler coverHandler;
    protected final MachineController machineController;

    @Networked
    private GameProfile owner;
    private boolean isPrivate;

    public CoverableBlockEntity(BlockEntityProvider provider, BlockPos pos, BlockState state) {
        super(provider, pos, state);

        this.coverHandler = addComponent(new CoverHandlerImpl<>(this, getCoverBlacklist(), this::canPlaceCoverAtSide));
        this.machineController = addComponent(new MachineControllerImpl(this));
    }

    public MachineController getMachineController() {
        return this.machineController;
    }

    protected Collection<CoverCategory> getCoverBlacklist() {
        return List.of();
    }
    
    protected boolean canPlaceCoverAtSide(Direction side) {
        return true;
    }

    @Override
    protected SlotAwareItemHandler createItemHandler() {
        return new MachineSlotItemHandler(this, this::onInventoryChanged);
    }

    @Override
    public void provideAdditionalDrops(List<? super ItemStack> drops) {
        super.provideAdditionalDrops(drops);

        StreamEx.ofValues(this.coverHandler.getCovers())
            .map(Cover::getItem)
            .map(ItemStack::new)
            .toListAndThen(drops::addAll);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        Map<Direction, Cover> covers = this.coverHandler.getCovers();
        return beforeUse(player, hand, hit)
            || StreamEx.ofValues(covers).anyMatch(cover -> cover.use(state, level, pos, player, hand, hit))
            || !StreamEx.ofValues(covers).cross(hit.getDirection()).allMatch(Cover::opensGui)
            ? InteractionResult.SUCCESS
            : super.use(state, level, pos, player, hand, hit);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);

        if (placer instanceof Player player) {
            setOwner(player.getGameProfile());
        }
    }

    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return this.machineController.getSignal(direction);
    }

    @Override
    protected void saveAdditional(FriendlyCompoundTag tag) {
        super.saveAdditional(tag);

        if (this.owner != null) {
            tag.put("owner", NbtUtils.writeGameProfile(new CompoundTag(), this.owner));
        }
        tag.putBoolean("isPrivate", this.isPrivate);
    }

    @Override
    protected void load(FriendlyCompoundTag tag) {
        super.load(tag);

        if (tag.contains("owner")) {
            this.owner = NbtUtils.readGameProfile(tag.getCompound("owner"));
        }
        this.isPrivate = tag.getBoolean("isPrivate");
    }

    @Override
    public List<Component> getScanInfo(Player player, BlockPos pos, int scanLevel) {
        List<Component> scan = new ArrayList<>();
        if (scanLevel > 1) {
            scan.add(GtLocale.key("info", checkAccess(player) ? "machine_accessible" : "machine_not_accessible").toComponent());
        }

        getScanInfoPre(scan, player, pos, scanLevel);
        StreamEx.of(getComponents())
            .forEach(component -> component.getScanInfo(scan, player, pos, scanLevel));
        getScanInfoPost(scan, player, pos, scanLevel);

        return scan;
    }

    public void getScanInfoPre(List<? super Component> scan, Player player, BlockPos pos, int scanLevel) {}

    public void getScanInfoPost(List<? super Component> scan, Player player, BlockPos pos, int scanLevel) {}

    public GameProfile getOwner() {
        return this.owner;
    }

    public void setOwner(GameProfile owner) {
        this.owner = owner;
    }

    public boolean isPrivate() {
        return this.isPrivate;
    }

    public void setPrivate(boolean value) {
        this.isPrivate = value;
    }

    public boolean isOwnedBy(GameProfile profile) {
        return this.owner.equals(profile);
    }

    public boolean checkAccess(Player player) {
        return checkAccess(player.getGameProfile());
    }

    public boolean checkAccess(GameProfile profile) {
        return !this.isPrivate || this.owner == null || this.owner.equals(profile);
    }

    protected boolean beforeUse(Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack stack = player.getItemInHand(hand);
        Direction side = hit.getDirection();

        if (!checkAccess(player)) {
            player.displayClientMessage(GtLocale.key("info", "access_error").toComponent(this.owner.getName()), true);
            return true;
        }

        if (GenericCover.isGenericCover(stack)) {
            return placeCover(ModCovers.GENERIC.get(), player, side, stack);
        }
        else if (VentCover.isVent(stack)) {
            return placeCover(ModCovers.VENT.get(), player, side, stack);
        }
        else if (stack.is(GregTechTags.SCREWDRIVER)) {
            return useScrewdriver(stack, side, player, hand);
        }
        return tryUseCrowbar(stack, side, player, hand);
    }

    private boolean placeCover(CoverType type, Player player, Direction side, ItemStack stack) {
        if (this.coverHandler.placeCoverAtSide(type, side, stack.getItem(), false)) {
            if (!player.isCreative()) stack.shrink(1);
            return true;
        }
        return false;
    }

    protected boolean useScrewdriver(ItemStack stack, Direction side, Player player, InteractionHand hand) {
        Cover existing = this.coverHandler.getCoverAtSide(side).orElse(null);
        if (existing != null) {
            CoverInteractionResult result = existing.onScrewdriverClick(player);
            if (!player.level.isClientSide && result == CoverInteractionResult.RERENDER) {
                GregTechNetwork.updateClientCover(this, existing);
            }
            if (result.isSuccess()) {
                if (result.isChanged()) {
                    setChanged();
                    GtUtil.hurtStack(stack, 1, player, hand);
                }
                return true;
            }
            return false;
        }
        return GtUtil.hurtStack(stack, 1, player, hand) && this.coverHandler.placeCoverAtSide(ModCovers.NORMAL.get(), side, null, false);
    }

    public boolean tryUseCrowbar(ItemStack stack, Direction side, Player player, InteractionHand hand) {
        return stack.is(GregTechTags.CROWBAR) && this.coverHandler.removeCover(side, false)
            .map(cover -> {
                GtUtil.hurtStack(stack, 1, player, hand);
                Item coverItem = cover.getItem();
                if (coverItem != null && !this.level.isClientSide) {
                    ItemEntity entity = new ItemEntity(
                        this.level,
                        this.worldPosition.getX() + side.getStepX() + 0.5,
                        this.worldPosition.getY() + side.getStepY() + 0.5,
                        this.worldPosition.getZ() + side.getStepZ() + 0.5,
                        new ItemStack(coverItem));
                    this.level.addFreshEntity(entity);
                }
                return true;
            })
            .orElse(false);
    }

    @NotNull
    @Override
    public ModelData getModelData() {
        return ModelData.builder()
            .with(CoverHandlerImpl.COVER_HANDLER_PROPERTY, this.coverHandler.getCovers())
            .build();
    }

    @Override
    public Optional<ItemStack> getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        if (target instanceof BlockHitResult blockHit) {
            return this.coverHandler.getCoverAtSide(blockHit.getDirection())
                .map(Cover::getItem)
                .map(ItemStack::new);
        }
        return super.getCloneItemStack(state, target, world, pos, player);
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction side) {
        return side != null && this.coverHandler.getCoverAtSide(side)
            .map(cover -> cover.letsRedstoneIn() || cover.letsRedstoneOut() || cover.acceptsRedstone() || cover.overrideRedstoneOut())
            .orElse(false);
    }
}
