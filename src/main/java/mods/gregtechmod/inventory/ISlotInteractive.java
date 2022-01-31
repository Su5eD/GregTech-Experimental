package mods.gregtechmod.inventory;

import mods.gregtechmod.util.ButtonClick;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface ISlotInteractive {
    boolean slotClick(ButtonClick click, EntityPlayer player, ItemStack stack);
}
