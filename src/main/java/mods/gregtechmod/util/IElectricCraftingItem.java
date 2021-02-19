package mods.gregtechmod.util;

import ic2.api.item.IElectricItem;
import net.minecraft.item.ItemStack;

public interface IElectricCraftingItem extends IElectricItem {
    boolean canUse(ItemStack stack);
}
