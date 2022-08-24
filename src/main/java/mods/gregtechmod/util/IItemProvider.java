package mods.gregtechmod.util;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface IItemProvider {
    String name();

    Item getInstance();
    
    default EnumRarity getRarity() {
        return EnumRarity.COMMON;
    }

    default ItemStack getItemStack() {
        return getItemStack(1);
    }

    default ItemStack getItemStack(int count) {
        return new ItemStack(getInstance(), count);
    }
}
