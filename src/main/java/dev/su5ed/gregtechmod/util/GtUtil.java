package dev.su5ed.gregtechmod.util;

import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

import java.util.Collection;
import java.util.EnumSet;

public final class GtUtil {
    public static final Collection<Direction> VERTICAL_FACINGS = EnumSet.of(Direction.DOWN, Direction.UP);
    public static final Collection<Direction> HORIZONTAL_FACINGS = EnumSet.of(Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST);

    private GtUtil() {}

    public static boolean isVerticalFacing(Direction direction) {
        return VERTICAL_FACINGS.contains(direction);
    }

    public static boolean isHorizontalFacing(Direction direction) {
        return HORIZONTAL_FACINGS.contains(direction);
    }

    public static Material getMaterial(ResourceLocation location) {
        return new Material(InventoryMenu.BLOCK_ATLAS, location);
    }
}
