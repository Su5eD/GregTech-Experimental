package dev.su5ed.gregtechmod.util.nbt;

import net.minecraft.nbt.Tag;

public interface NBTSerializer<T, U extends Tag> {
    U serialize(T value);
}
