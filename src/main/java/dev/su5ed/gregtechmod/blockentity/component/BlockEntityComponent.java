package dev.su5ed.gregtechmod.blockentity.component;

import dev.su5ed.gregtechmod.api.util.NBTTarget;
import dev.su5ed.gregtechmod.blockentity.base.BaseBlockEntity;
import dev.su5ed.gregtechmod.util.nbt.FieldUpdateListener;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public interface BlockEntityComponent extends FieldUpdateListener {
    BaseBlockEntity getParent();
    
    ResourceLocation getName();
    
    CompoundTag save(NBTTarget target);
    
    void load(CompoundTag tag, boolean notifyListeners);
}
