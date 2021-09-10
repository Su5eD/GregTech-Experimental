package mods.gregtechmod.util.nbt;

import net.minecraft.nbt.NBTBase;

public interface INBTSerializer<T, U extends NBTBase> {
    
    U serialize(T value);
    
    T deserialize(U nbt, Object instance, Class<?> cls);
}
