package dev.su5ed.gregtechmod.blockentity;

import com.google.common.base.Preconditions;
import dev.su5ed.gregtechmod.api.cover.CoverType;
import dev.su5ed.gregtechmod.api.cover.ICover;
import dev.su5ed.gregtechmod.api.cover.ICoverable;
import dev.su5ed.gregtechmod.blockentity.component.CoverHandler;
import dev.su5ed.gregtechmod.util.BlockEntityProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
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

public class CoverableBlockEntity extends BaseBlockEntity implements ICoverable {
    private final CoverHandler<CoverableBlockEntity> coverHandler;
    private final Collection<CoverType> coverBlacklist;

    public CoverableBlockEntity(BlockEntityProvider provider, BlockPos pos, BlockState state) {
        super(provider, pos, state);

        this.coverHandler = addComponent(new CoverHandler<>(this));
        this.coverBlacklist = getCoverBlacklist();
    }

    protected Collection<CoverType> getCoverBlacklist() {
        return List.of();
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
                .map(ICover::getItem)
                .map(ItemStack::new);
        }
        return super.getCloneItemStack(state, target, world, pos, player);
    }

    @Override
    public Collection<? extends ICover> getCovers() {
        return this.coverHandler.getCovers().values();
    }

    @Override
    public Optional<ICover> getCoverAtSide(Direction side) {
        return this.coverHandler.getCoverAtSide(side);
    }

    @Override
    public boolean placeCoverAtSide(ICover cover, Player player, Direction side, boolean simulate) {
        return !this.coverBlacklist.contains(cover.getType()) && this.coverHandler.placeCoverAtSide(cover, side, simulate);
    }

    @Override
    public boolean removeCover(Direction side, boolean simulate) {
        return !this.level.isClientSide && getCoverAtSide(side)
            .map(cover -> {
                Item coverItem = cover.getItem();
                if (this.coverHandler.removeCover(side, false)) {
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
                }
                return false;
            })
            .orElse(false);
    }

    @Override
    public void updateRender() {
        Preconditions.checkState(!this.level.isClientSide, "Render must only be updated from server side");
        
        BlockState state = getBlockState();
        this.level.sendBlockUpdated(this.worldPosition, state, state, Block.UPDATE_IMMEDIATE);
    }
}
