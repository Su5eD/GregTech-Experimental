package mods.gregtechmod.api.recipe;

import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;

import java.util.List;

public interface IRecipeFusion<I extends IRecipeIngredient, T> extends IMachineRecipe<List<I>, T> {
    double getStartEnergy();
}
