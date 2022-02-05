package dev.su5ed.gregtechmod.api.recipe;

import dev.su5ed.gregtechmod.api.recipe.ingredient.IRecipeIngredient;

import java.util.List;

public interface IRecipeFusion<I extends IRecipeIngredient, T> extends IMachineRecipe<List<I>, T> {
    double getStartEnergy();
}
