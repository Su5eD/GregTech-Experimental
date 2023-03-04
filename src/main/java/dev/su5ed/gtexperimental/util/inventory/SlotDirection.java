package dev.su5ed.gtexperimental.util.inventory;

import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public enum SlotDirection implements Predicate<@Nullable Direction> {
    ANY(Direction.values()),
    TOP(Direction.UP),
    BOTTOM(Direction.DOWN),     
    SIDE(Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST),
    FRONT_BACK(Direction.NORTH, Direction.SOUTH),
    LEFT_RIGHT(Direction.WEST, Direction.EAST),
    VERTICAL(Direction.UP, Direction.DOWN),
    NONE();

    private final Collection<Direction> sides;

    SlotDirection(final Direction... sides) {
        this.sides = List.of(sides);
    }
    
    @Override
    public boolean test(@Nullable Direction side) {
        return side == null || this.sides.contains(side);
    }
}
