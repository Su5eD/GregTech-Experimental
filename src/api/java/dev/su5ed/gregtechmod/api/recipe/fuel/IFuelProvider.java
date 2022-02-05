package dev.su5ed.gregtechmod.api.recipe.fuel;

import dev.su5ed.gregtechmod.api.recipe.ingredient.IRecipeIngredient;

public interface IFuelProvider<T extends IFuel<? extends IRecipeIngredient>, I> {
    T getFuel(I target);
        
    boolean hasFuel(I target);
}
