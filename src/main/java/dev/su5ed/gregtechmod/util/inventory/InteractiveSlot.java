package dev.su5ed.gregtechmod.util.inventory;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface InteractiveSlot {
    void slotClick(ButtonClick click, Player player, ItemStack stack);
    
    void slotScroll(Player player, ScrollDirection direction, boolean shift);
}
