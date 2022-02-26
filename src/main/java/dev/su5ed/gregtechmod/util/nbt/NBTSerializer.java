package dev.su5ed.gregtechmod.util.nbt;

import dev.su5ed.gregtechmod.api.util.NBTTarget;
import net.minecraft.nbt.Tag;

public interface NBTSerializer<T, U extends Tag> {
    U serialize(T value, NBTTarget target);
}
