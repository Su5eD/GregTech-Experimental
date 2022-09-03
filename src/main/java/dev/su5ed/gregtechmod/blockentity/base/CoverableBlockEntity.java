package dev.su5ed.gregtechmod.blockentity.base;

import dev.su5ed.gregtechmod.Capabilities;
import dev.su5ed.gregtechmod.GregTechTags;
import dev.su5ed.gregtechmod.api.cover.Cover;
import dev.su5ed.gregtechmod.api.cover.CoverCategory;
import dev.su5ed.gregtechmod.api.cover.CoverHandler;
import dev.su5ed.gregtechmod.api.cover.CoverType;
import dev.su5ed.gregtechmod.api.cover.CoverInteractionResult;
import dev.su5ed.gregtechmod.blockentity.component.CoverHandlerImpl;
import dev.su5ed.gregtechmod.cover.GenericCover;
import dev.su5ed.gregtechmod.cover.VentCover;
import dev.su5ed.gregtechmod.network.GregTechNetwork;
import dev.su5ed.gregtechmod.network.Networked;
import dev.su5ed.gregtechmod.object.ModCovers;
import dev.su5ed.gregtechmod.util.BlockEntityProvider;
import dev.su5ed.gregtechmod.util.GtUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import one.util.streamex.StreamEx;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CoverableBlockEntity extends BaseBlockEntity {
    @Networked
    private final CoverHandlerImpl<CoverableBlockEntity> coverHandler;
    private final LazyOptional<CoverHandler> optionalCoverHandler;

    public CoverableBlockEntity(BlockEntityProvider provider, BlockPos pos, BlockState state) {
        super(provider, pos, state);

        this.coverHandler = addComponent(new CoverHandlerImpl<>(this, getCoverBlacklist()));
        this.optionalCoverHandler = LazyOptional.of(() -> this.coverHandler);
    }

    protected Collection<CoverCategory> getCoverBlacklist() {
        return List.of();
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        Map<Direction, Cover<?>> covers = this.coverHandler.getCovers();
        return beforeUse(player, hand, hit)
            || StreamEx.ofValues(covers).anyMatch(cover -> cover.use(state, level, pos, player, hand, hit))
            || !StreamEx.ofValues(covers).cross(hit.getDirection()).allMatch(Cover::opensGui)
            ? InteractionResult.SUCCESS
            : super.use(state, level, pos, player, hand, hit);
    }

    protected boolean beforeUse(Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack stack = player.getItemInHand(hand);
        Direction side = hit.getDirection();
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

    private boolean placeCover(CoverType<?> type, Player player, Direction side, ItemStack stack) {
        if (this.coverHandler.placeCoverAtSide(type, side, stack.getItem(), false)) {
            if (!player.isCreative()) stack.shrink(1);
            return true;
        }
        return false;
    }

    protected boolean useScrewdriver(ItemStack stack, Direction side, Player player, InteractionHand hand) {
        Cover<?> existing = this.coverHandler.getCoverAtSide(side).orElse(null);
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
        if (stack.is(GregTechTags.CROWBAR)) {
            return this.coverHandler.removeCover(side, false)
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
        return false;
    }

    @NotNull
    @Override
    public IModelData getModelData() {
        return new ModelDataMap.Builder()
            .withInitial(CoverHandlerImpl.COVER_HANDLER_PROPERTY, this.coverHandler.getCovers())
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

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return Capabilities.COVERABLE.orEmpty(cap, this.optionalCoverHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.optionalCoverHandler.invalidate();
    }
}
