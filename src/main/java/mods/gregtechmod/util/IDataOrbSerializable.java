package mods.gregtechmod.util;

import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;

public interface IDataOrbSerializable {
    
    String getDataName();
    
    @Nullable
    NBTTagCompound saveDataToOrb();
    
    void loadDataFromOrb(NBTTagCompound nbt);
}
