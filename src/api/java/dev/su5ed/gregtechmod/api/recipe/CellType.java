package dev.su5ed.gregtechmod.api.recipe;

import ic2.core.ref.Ic2Items;
import net.minecraft.world.item.ItemStack;

import java.util.function.BiPredicate;

public enum CellType {
    CELL((stack, classic) -> /* TODO stack.isItemEqual(Ic2Items.FLUID_CELL) && stack.getTag() == null */ classic && stack.getItem() == Ic2Items.EMPTY_CELL),
    FUEL_CAN((stack, classic) -> stack.getItem() == Ic2Items.EMPTY_FUEL_CAN);

    private final BiPredicate<ItemStack, Boolean> matcher;

    CellType(BiPredicate<ItemStack, Boolean> matcher) {
        this.matcher = matcher;
    }
    
    public boolean apply(ItemStack stack, boolean classic) {
        return !stack.isEmpty() && this.matcher.test(stack, classic);
    }
}
