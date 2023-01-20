package dev.su5ed.gtexperimental.api.recipe.fuel;

import dev.su5ed.gtexperimental.api.recipe.ingredient.IRecipeIngredient;

public interface IFuelProvider<T extends IFuel<? extends IRecipeIngredient>, I> {
    T getFuel(I target);
        
    boolean hasFuel(I target);
}
