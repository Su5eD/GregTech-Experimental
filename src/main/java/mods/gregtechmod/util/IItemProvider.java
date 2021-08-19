package mods.gregtechmod.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface IItemProvider {
    String name();

    Item getInstance();
    
    default ItemStack getItemStack() {
        return new ItemStack(getInstance());
    }
}
