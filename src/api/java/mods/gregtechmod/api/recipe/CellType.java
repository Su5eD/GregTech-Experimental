package mods.gregtechmod.api.recipe;

import ic2.api.item.IC2Items;
import mods.gregtechmod.api.GregTechAPI;
import net.minecraft.item.ItemStack;

import java.util.function.Predicate;

public enum CellType {
    CELL(stack -> GregTechAPI.isClassic ? stack.isItemEqual(IC2Items.getItem("cell", "empty")) : stack.isItemEqual(IC2Items.getItem("fluid_cell")) && stack.getTagCompound() == null),
    FUEL_CAN(stack -> stack.isItemEqual(IC2Items.getItem("crafting", "empty_fuel_can")));

    private final Predicate<ItemStack> matcher;

    CellType(Predicate<ItemStack> matcher) {
        this.matcher = matcher;
    }

    public boolean apply(ItemStack stack) {
        return !stack.isEmpty() && this.matcher.test(stack);
    }
}
