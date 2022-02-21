package dev.su5ed.gregtechmod.blockentity;

import dev.su5ed.gregtechmod.api.cover.CoverType;
import dev.su5ed.gregtechmod.api.cover.ICover;
import dev.su5ed.gregtechmod.api.cover.ICoverable;
import dev.su5ed.gregtechmod.blockentity.component.CoverHandler;
import dev.su5ed.gregtechmod.util.BlockEntityProvider;
import dev.su5ed.gregtechmod.util.nbt.NBTPersistent;
import dev.su5ed.gregtechmod.util.nbt.NBTPersistent.Mode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class CoverableBlockEntity extends BaseBlockEntity implements ICoverable {
    @NBTPersistent(mode = Mode.BOTH)
    private final CoverHandler<CoverableBlockEntity> coverHandler;
    private final Collection<CoverType> coverBlacklist;

    public CoverableBlockEntity(BlockEntityProvider provider, BlockPos pos, BlockState state) {
        super(provider, pos, state);

        this.coverHandler = new CoverHandler<>(this, () -> updateClientField("coverHandler"));
        this.coverBlacklist = getCoverBlacklist();
    }
    
    protected Collection<CoverType> getCoverBlacklist() {
        return List.of();
    }

    @Override
    public Optional<ItemStack> getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        if (target instanceof BlockHitResult blockHit) {
            return getCoverAtSide(blockHit.getDirection())
                .map(ICover::getItem);
        }
        return super.getCloneItemStack(state, target, world, pos, player);
    }

    @Override
    public Collection<? extends ICover> getCovers() {
        return this.coverHandler.getCovers();
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
                ItemStack coverItem = cover.getItem();
                if (this.coverHandler.removeCover(side, false)) {
                    if (coverItem != null && !this.level.isClientSide) {
                        ItemEntity entity = new ItemEntity(
                            this.level,
                            this.worldPosition.getX() + side.getStepX() + 0.5,
                            this.worldPosition.getY() + side.getStepY() + 0.5,
                            this.worldPosition.getZ() + side.getStepZ() + 0.5,
                            coverItem);
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
        // TODO
    }
}
