package mods.gregtechmod.api.recipe.manager;

import mods.gregtechmod.api.recipe.IRecipeGrinder;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface IGtRecipeManagerGrinder extends IGtRecipeManager<IRecipeIngredient, ItemStack, IRecipeGrinder> {
    IRecipeGrinder getRecipeFor(ItemStack stack, FluidStack fluid);
}
