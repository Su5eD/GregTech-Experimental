package dev.su5ed.gregtechmod.blockentity.component;

import dev.su5ed.gregtechmod.api.util.FriendlyCompoundTag;
import dev.su5ed.gregtechmod.blockentity.base.BaseBlockEntity;
import dev.su5ed.gregtechmod.network.FieldUpdateListener;
import net.minecraft.resources.ResourceLocation;

public interface BlockEntityComponent extends FieldUpdateListener {
    BaseBlockEntity getParent();
    
    ResourceLocation getName();
    
    default FriendlyCompoundTag save() {
        FriendlyCompoundTag tag = new FriendlyCompoundTag();
        save(tag);
        return tag;
    }
    
    default void save(FriendlyCompoundTag tag) {}
    
    default void load(FriendlyCompoundTag tag) {}
}
