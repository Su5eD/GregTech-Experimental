package dev.su5ed.gregtechmod.api.util;

import net.minecraft.world.level.ItemLike;

public record SonictronSound(String name, ItemLike item, int count) {
    public SonictronSound(String name, ItemLike item) {
        this(name, item, 1);
    }
}
