package dev.su5ed.gregtechmod.blockentity.base;

import dev.su5ed.gregtechmod.GregTechTags;
import dev.su5ed.gregtechmod.api.cover.Cover;
import dev.su5ed.gregtechmod.api.cover.CoverCategory;
import dev.su5ed.gregtechmod.api.cover.Coverable;
import dev.su5ed.gregtechmod.api.util.CoverInteractionResult;
import dev.su5ed.gregtechmod.blockentity.component.CoverHandler;
import dev.su5ed.gregtechmod.cover.GenericCover;
import dev.su5ed.gregtechmod.cover.ModCoverType;
import dev.su5ed.gregtechmod.cover.VentCover;
import dev.su5ed.gregtechmod.network.GregTechNetwork;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class CoverableBlockEntity extends BaseBlockEntity implements Coverable {
    private final CoverHandler<CoverableBlockEntity> coverHandler;
    private final Collection<CoverCategory> coverBlacklist;

    public CoverableBlockEntity(BlockEntityProvider provider, BlockPos pos, BlockState state) {
        super(provider, pos, state);

        this.coverHandler = addComponent(new CoverHandler<>(this));
        this.coverBlacklist = getCoverBlacklist();
    }

    protected Collection<CoverCategory> getCoverBlacklist() {
        return List.of();
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return beforeUse(player, hand, hit)
            ? InteractionResult.SUCCESS
            : super.use(state, level, pos, player, hand, hit);
    }

    protected boolean beforeUse(Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack stack = player.getItemInHand(hand);
        Direction side = hit.getDirection();
        if (GenericCover.isGenericCover(stack)) {
            return placeCover(ModCoverType.GENERIC, player, side, stack);
        }
        else if (VentCover.isVent(stack)) {
            return placeCover(ModCoverType.VENT, player, side, stack);
        }
        else if (stack.is(GregTechTags.SCREWDRIVER)) {
            return useScrewdriver(stack, side, player);
        }
        return tryUseCrowbar(stack, side, player);
    }

    private boolean placeCover(ModCoverType type, Player player, Direction side, ItemStack stack) {
        Cover cover = type.get().create(this, side, stack.getItem());
        if (placeCoverAtSide(cover, player, side, false)) {
            if (!player.isCreative()) stack.shrink(1);
            return true;
        }
        return false;
    }

    protected boolean useScrewdriver(ItemStack stack, Direction side, Player player) {
        boolean success = getCoverAtSide(side)
            .map(cover -> {
                CoverInteractionResult result = cover.onScrewdriverClick(player);
                if (result == CoverInteractionResult.UPDATE) {
                    GregTechNetwork.updateClientCover(this, cover);
                }
                return result.isSuccess();
            })
            .orElse(false);
        if (success) {
            setChanged();
            GtUtil.hurtStack(stack, 1, player);
            return true;
        }

        Cover cover = ModCoverType.NORMAL.get().create(this, side, Items.AIR);
        return placeCoverAtSide(cover, player, side, false);
    }

    public boolean tryUseCrowbar(ItemStack stack, Direction side, Player player) {
        if (stack.is(GregTechTags.CROWBAR) && removeCover(side, false)) {
            GtUtil.hurtStack(stack, 1, player);
            return true;
        }
        return false;
    }

    @Nonnull
    @Override
    public IModelData getModelData() {
        return new ModelDataMap.Builder()
            .withInitial(CoverHandler.COVER_HANDLER_PROPERTY, this.coverHandler.getCovers())
            .build();
    }

    @Override
    public Optional<ItemStack> getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        if (target instanceof BlockHitResult blockHit) {
            return getCoverAtSide(blockHit.getDirection())
                .map(Cover::getItem)
                .map(ItemStack::new);
        }
        return super.getCloneItemStack(state, target, world, pos, player);
    }

    @Override
    public Collection<? extends Cover> getCovers() {
        return this.coverHandler.getCovers().values();
    }

    @Override
    public Optional<Cover> getCoverAtSide(Direction side) {
        return this.coverHandler.getCoverAtSide(side);
    }

    @Override
    public boolean placeCoverAtSide(Cover cover, Player player, Direction side, boolean simulate) {
        return !this.coverBlacklist.contains(cover.getCategory()) && this.coverHandler.placeCoverAtSide(cover, side, simulate);
    }

    @Override
    public boolean removeCover(Direction side, boolean simulate) {
        return getCoverAtSide(side)
            .map(cover -> {
                Item coverItem = cover.getItem();
                if (this.coverHandler.removeCover(side, false)) {
                    if (coverItem != Items.AIR && !this.level.isClientSide) {
                        ItemEntity entity = new ItemEntity(
                            this.level,
                            this.worldPosition.getX() + side.getStepX() + 0.5,
                            this.worldPosition.getY() + side.getStepY() + 0.5,
                            this.worldPosition.getZ() + side.getStepZ() + 0.5,
                            new ItemStack(coverItem));
                        this.level.addFreshEntity(entity);
                    }
                    return true;
                }
                return false;
            })
            .orElse(false);
    }

    @Override
    public void updateRender() {
        GtUtil.assertServerSide(this.level);

        BlockState state = getBlockState();
        this.level.sendBlockUpdated(this.worldPosition, state, state, Block.UPDATE_IMMEDIATE);
    }
}