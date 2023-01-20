package dev.su5ed.gtexperimental.api.recipe;

import dev.su5ed.gtexperimental.api.recipe.ingredient.IRecipeIngredient;

import java.util.List;

public interface IRecipeFusion<I extends IRecipeIngredient, T> extends IMachineRecipe<List<I>, T> {
    double getStartEnergy();
}
