package dev.su5ed.gregtechmod.util;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.Locale;

public interface ItemProvider extends ItemLike {
    default String getName() {
        return name().toLowerCase(Locale.ROOT);
    }
    
    @Override
    default Item asItem() {
        return getItem();
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
