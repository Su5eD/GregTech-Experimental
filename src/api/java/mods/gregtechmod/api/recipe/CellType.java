package mods.gregtechmod.api.recipe;

import ic2.api.item.IC2Items;
import net.minecraft.item.ItemStack;

import java.util.function.BiPredicate;

public enum CellType {
    CELL((stack, classic) -> classic ? stack.isItemEqual(IC2Items.getItem("cell", "empty")) : stack.isItemEqual(IC2Items.getItem("fluid_cell")) && stack.getTagCompound() == null),
    FUEL_CAN((stack, classic) -> stack.isItemEqual(IC2Items.getItem("crafting", "empty_fuel_can")));

    private final BiPredicate<ItemStack, Boolean> matcher;

    CellType(BiPredicate<ItemStack, Boolean> matcher) {
        this.matcher = matcher;
    }
    
    public boolean apply(ItemStack stack, boolean classic) {
        return !stack.isEmpty() && this.matcher.test(stack, classic);
    }
}
