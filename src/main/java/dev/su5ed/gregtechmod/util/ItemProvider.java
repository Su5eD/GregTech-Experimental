package dev.su5ed.gregtechmod.util;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Locale;

public interface ItemProvider {
    default String getName() {
        return name().toLowerCase(Locale.ROOT);
    }
    
    String getRegistryName();

    Item getItem();
    
    String name();

    default ItemStack getItemStack() {
        return getItemStack(1);
    }

    default ItemStack getItemStack(int count) {
        return new ItemStack(getItem(), count);
    }
}
