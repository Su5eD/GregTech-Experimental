package dev.su5ed.gregtechmod.util.nbt;

import net.minecraft.nbt.Tag;

public interface NBTDeserializer<T, U extends Tag, V> {
    T deserialize(U tag, V instance, Class<?> cls);
}
