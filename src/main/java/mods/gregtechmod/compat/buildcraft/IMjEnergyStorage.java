package mods.gregtechmod.compat.buildcraft;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public interface IMjEnergyStorage extends INBTSerializable<NBTTagCompound> {
    long getStored();

    long getCapacity();

    void setCapacity(long capacity);

    long addPower(long microJoulesToAdd, boolean simulate);

    boolean extractPower(long power);
}
