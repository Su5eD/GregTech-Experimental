package mods.gregtechmod.util.nbt;

import net.minecraft.nbt.NBTBase;

public interface INBTHandler<T, U extends NBTBase> extends INBTSerializer<T, U>, INBTDeserializer<T, U> {

}
