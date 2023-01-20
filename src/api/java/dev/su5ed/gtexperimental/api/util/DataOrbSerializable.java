package dev.su5ed.gtexperimental.api.util;

import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.Nullable;

public interface DataOrbSerializable {
    String getDataName();
    
    @Nullable
    CompoundTag saveDataToOrb();
    
    void loadDataFromOrb(CompoundTag tag);
}
