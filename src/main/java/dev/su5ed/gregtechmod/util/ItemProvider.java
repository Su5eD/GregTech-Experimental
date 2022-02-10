package dev.su5ed.gregtechmod.util;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface ItemProvider {
    String getName();

    Item getItem();

    default ItemStack getItemStack() {
        return getItemStack(1);
    }

    default ItemStack getItemStack(int count) {
        return new ItemStack(getItem(), count);
    }
}
