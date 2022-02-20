package dev.su5ed.gregtechmod.util.nbt;

import net.minecraft.nbt.Tag;

public interface NBTHandler<T, U extends Tag> extends NBTSerializer<T, U>, NBTDeserializer<T, U> {

}
