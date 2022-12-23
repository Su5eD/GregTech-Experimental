package dev.su5ed.gregtechmod.blockentity.component;

import dev.su5ed.gregtechmod.api.util.FriendlyCompoundTag;
import dev.su5ed.gregtechmod.blockentity.base.BaseBlockEntity;
import dev.su5ed.gregtechmod.network.FieldUpdateListener;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import java.util.List;

public interface BlockEntityComponent extends FieldUpdateListener, ICapabilityProvider {
    BaseBlockEntity getParent();
    
    ResourceLocation getName();
    
    default int getPriority() {
        return 0;
    }
    
    void onLoad();
    
    void onUnload();
    
    void invalidateCaps();
    
    void tickServer();
    
    void getScanInfo(List<Component> scan, Player player, BlockPos pos, int scanLevel);

    default FriendlyCompoundTag save() {
        FriendlyCompoundTag tag = new FriendlyCompoundTag();
        save(tag);
        return tag;
    }
    
    default void save(FriendlyCompoundTag tag) {}
    
    default void load(FriendlyCompoundTag tag) {}
}
