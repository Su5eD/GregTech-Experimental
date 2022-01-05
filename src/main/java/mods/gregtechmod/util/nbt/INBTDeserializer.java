package mods.gregtechmod.util.nbt;

import net.minecraft.nbt.NBTBase;

public interface INBTDeserializer<T, U extends NBTBase> {
    T deserialize(U nbt, Object instance, Class<?> cls);
}
