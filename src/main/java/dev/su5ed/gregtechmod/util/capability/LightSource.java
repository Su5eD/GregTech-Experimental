package dev.su5ed.gregtechmod.util.capability;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;

public interface LightSource extends INBTSerializable<CompoundTag> {
    @Nullable
    BlockPos getSourcePos();
    
    void setSourcePos(BlockPos pos);
}
