package mods.gregtechmod.api.recipe;

import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import net.minecraft.item.ItemStack;

import java.util.List;

public interface IRecipeGrinder extends IMachineRecipe<IRecipeIngredient, List<ItemStack>> {
    IRecipeIngredientFluid getFluid();
}
