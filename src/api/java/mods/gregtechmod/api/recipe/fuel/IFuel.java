package mods.gregtechmod.api.recipe.fuel;

import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;

public interface IFuel<I extends IRecipeIngredient, O> {
    I getInput();

    double getEnergy();

    O getOutput();

    boolean isInvalid();
}
