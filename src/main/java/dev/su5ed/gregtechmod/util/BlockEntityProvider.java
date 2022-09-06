package dev.su5ed.gregtechmod.util;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Collection;
import java.util.function.Function;

public interface BlockEntityProvider {
    BlockEntityType<?> getType();

    AllowedFacings getAllowedFacings();

    enum AllowedFacings {
        ALL(GtUtil.ALL_FACINGS, BlockPlaceContext::getNearestLookingDirection),
        HORIZONTAL(GtUtil.HORIZONTAL_FACINGS, UseOnContext::getHorizontalDirection),
        VERTICAL(GtUtil.VERTICAL_FACINGS, BlockPlaceContext::getNearestLookingVerticalDirection),
        NORTH(GtUtil.NORTH_FACING, ctx -> Direction.NORTH);

        private final Collection<Direction> facings;
        private final Function<BlockPlaceContext, Direction> function;

        AllowedFacings(Collection<Direction> facings, Function<BlockPlaceContext, Direction> function) {
            this.facings = facings;
            this.function = function;
        }
        
        public boolean allows(Direction facing) {
            return this.facings.contains(facing);
        }

        public Direction getFacing(BlockPlaceContext context) {
            return this.function.apply(context).getOpposite();
        }
    }
}