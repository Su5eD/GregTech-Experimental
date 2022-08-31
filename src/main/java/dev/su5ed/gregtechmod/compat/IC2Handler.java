package dev.su5ed.gregtechmod.compat;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.List;

public final class IC2Handler {
    
    public static <T extends Item & IElectricItem> List<ItemStack> getChargedVariants(T item) {
        return Arrays.asList(getChargedStack(item, 0), getChargedStack(item, Double.MAX_VALUE));
    }
    
    private static <T extends Item & IElectricItem> ItemStack getChargedStack(T item, double charge) {
        ItemStack stack = new ItemStack(item);
        ElectricItem.manager.charge(stack, charge, Integer.MAX_VALUE, true, false);
        return stack;
    }
    
    private IC2Handler() {}
}
