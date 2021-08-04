package mods.gregtechmod.api.recipe.fuel;

import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;

public interface IFuelProvider<T extends IFuel<? extends IRecipeIngredient>, I> {
    T getFuel(I target);
        
    boolean hasFuel(I target);
}
