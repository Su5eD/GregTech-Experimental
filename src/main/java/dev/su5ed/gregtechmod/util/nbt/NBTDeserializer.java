package dev.su5ed.gregtechmod.util.nbt;

import net.minecraft.nbt.Tag;

public interface NBTDeserializer<T, U extends Tag> {
    T deserialize(U nbt, Object instance, Class<?> cls);
}
