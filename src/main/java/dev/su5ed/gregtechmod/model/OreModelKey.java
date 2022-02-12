package dev.su5ed.gregtechmod.model;

import net.minecraft.core.Direction;

import java.util.Map;

public record OreModelKey(Map<Direction, Direction> sideOverrides, Texture texture) {
    
    public enum Texture {
        DEFAULT,
        STONE,
        NETHERRACK,
        END_STONE
    }
}
