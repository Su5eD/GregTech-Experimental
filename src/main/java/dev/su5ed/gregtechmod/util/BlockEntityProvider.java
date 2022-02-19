package dev.su5ed.gregtechmod.util;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Function;

public interface BlockEntityProvider {
    BlockEntityType<?> getType();

    AllowedFacings getAllowedFacings();

    enum AllowedFacings {
        ALL(BlockPlaceContext::getNearestLookingDirection),
        HORIZONTAL(UseOnContext::getHorizontalDirection),
        VERTICAL(BlockPlaceContext::getNearestLookingVerticalDirection),
        NORTH(ctx -> Direction.NORTH);

        private final Function<BlockPlaceContext, Direction> function;

        AllowedFacings(Function<BlockPlaceContext, Direction> function) {
            this.function = function;
        }

        public Direction getFacing(BlockPlaceContext context) {
            return this.function.apply(context).getOpposite();
        }
    }
}
