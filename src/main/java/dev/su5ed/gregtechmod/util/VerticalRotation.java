package dev.su5ed.gregtechmod.util;

import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.EnumProperty;

import java.util.Locale;
import java.util.function.BinaryOperator;

public enum VerticalRotation implements StringRepresentable {
    MIRROR_BACK((face, side) -> side == Direction.NORTH ? Direction.SOUTH : side),
    ROTATE_X((face, side) -> {
        Direction target = face == Direction.UP ? Direction.NORTH : Direction.SOUTH;

        if (side == target) return face.getOpposite();
        else if (side == target.getOpposite()) return face;
        else if (side == face.getOpposite()) return Direction.SOUTH;
        return side;
    });
    
    public static final EnumProperty<VerticalRotation> ROTATION_PROPERTY = EnumProperty.create("vertical_rotation", VerticalRotation.class);

    public final BinaryOperator<Direction> rotation;

    VerticalRotation(BinaryOperator<Direction> rotation) {
        this.rotation = rotation;
    }

    @Override
    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
