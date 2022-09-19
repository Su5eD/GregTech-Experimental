package dev.su5ed.gregtechmod.compat;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

final class IC2Handler {

    static List<ItemStack> getChargedVariants(Item item) {
        return Arrays.asList(getChargedStack(item, 0), getChargedStack(item, Double.MAX_VALUE));
    }

    static ItemStack getChargedStack(Item item, double charge) {
        ItemStack stack = new ItemStack(item);
        charge(stack, charge, Integer.MAX_VALUE, true, false);
        return stack;
    }

    static boolean isEnergyItem(Item item) {
        return item instanceof IElectricItem;
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

    static boolean use(ItemStack stack, double energy, @Nullable LivingEntity user) {
        return ElectricItem.manager.use(stack, energy, user);
    }

    static String getEnergyTooltip(ItemStack stack) {
        return ElectricItem.manager.getToolTip(stack);
    }

    static void depleteStackEnergy(ItemStack stack) {
        if (stack.getItem() instanceof IElectricItem) {
            ElectricItem.manager.discharge(stack, Double.MAX_VALUE, Integer.MAX_VALUE, true, false, false);
        }
    }
    
    static double charge(ItemStack stack, double amount, int tier, boolean ignoreTransferLimit, boolean simulate) {
        return ElectricItem.manager.charge(stack, amount, tier, ignoreTransferLimit, simulate);
    }

    private IC2Handler() {}
}
