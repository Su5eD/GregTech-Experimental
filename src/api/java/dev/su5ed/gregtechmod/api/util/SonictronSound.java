package dev.su5ed.gregtechmod.api.util;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

public record SonictronSound(String name, ItemLike item, int count) {

    public SonictronSound(SoundEvent sound, ItemLike item) {
        this(ForgeRegistries.SOUND_EVENTS.getKey(sound).toString(), item, 1);
    }

    public SonictronSound(SoundEvent sound, ItemLike item, int count) {
        this(ForgeRegistries.SOUND_EVENTS.getKey(sound).toString(), item, count);
    }
}
