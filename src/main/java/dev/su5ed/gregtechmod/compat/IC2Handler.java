package dev.su5ed.gregtechmod.compat;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.minecraft.world.entity.LivingEntity;
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

    static double getCharge(ItemStack stack) {
        return ElectricItem.manager.getCharge(stack);
    }
    
    static double getChargeLevel(ItemStack stack) {
        return ElectricItem.manager.getChargeLevel(stack);
    }
    
    static boolean canUse(ItemStack stack, double energy) {
        return ElectricItem.manager.canUse(stack, energy);
    }
    
    static boolean use(ItemStack stack, double energy, LivingEntity user) {
        return ElectricItem.manager.use(stack, energy, user);
    }
    
    static String getEnergyTooltip(ItemStack stack) {
        return ElectricItem.manager.getToolTip(stack);
    }
    
    private IC2Handler() {}
}
