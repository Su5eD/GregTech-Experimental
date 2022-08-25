package dev.su5ed.gregtechmod.api.util;

import net.minecraft.nbt.CompoundTag;

import javax.annotation.Nullable;

public interface IDataOrbSerializable {
    
    String getDataName();
    
    @Nullable
    CompoundTag saveDataToOrb();
    
    void loadDataFromOrb(CompoundTag tag);
}