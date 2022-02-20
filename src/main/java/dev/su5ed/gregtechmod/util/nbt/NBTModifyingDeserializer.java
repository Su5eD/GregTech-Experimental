package dev.su5ed.gregtechmod.util.nbt;

import net.minecraft.nbt.Tag;

public interface NBTModifyingDeserializer<T, U extends Tag> {
    void modifyValue(T value, U nbt);
}
