package mods.gregtechmod.inventory;

import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;

public interface ISlotInteractive {
    boolean slotClick(ClickType clickType, ItemStack stack);
}
