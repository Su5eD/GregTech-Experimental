package mods.gregtechmod.util.nbt;

import net.minecraft.nbt.NBTBase;

public interface INBTModifyingDeserializer<T, U extends NBTBase> {

    void modifyValue(T value, U nbt);
}
