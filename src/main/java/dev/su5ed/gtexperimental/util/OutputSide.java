package dev.su5ed.gtexperimental.util;

import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public enum OutputSide implements StringRepresentable {
    NONE(null),
    DOWN(Direction.DOWN),
    UP(Direction.UP),
    NORTH(Direction.NORTH),
    SOUTH(Direction.SOUTH),
    WEST(Direction.WEST),
    EAST(Direction.EAST);

    @Nullable
    public final Direction direction;

    OutputSide(Direction direction) {
        this.direction = direction;
    }

    @Override
    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }

    public static OutputSide fromDirection(Direction direction) {
        return direction == null ? NONE : valueOf(direction.name());
    }
}
