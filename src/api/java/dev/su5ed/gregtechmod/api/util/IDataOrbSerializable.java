package dev.su5ed.gregtechmod.api.util;

import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.Nullable;

public interface IDataOrbSerializable {
    String getDataName();
    
    @Nullable
    CompoundTag saveDataToOrb();
    
    void loadDataFromOrb(CompoundTag tag);
}
