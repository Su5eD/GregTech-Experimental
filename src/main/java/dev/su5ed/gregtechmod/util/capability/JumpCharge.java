package dev.su5ed.gregtechmod.util.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface JumpCharge extends INBTSerializable<CompoundTag> {
    double getCharge();
    
    void setCharge(double charge);
}
