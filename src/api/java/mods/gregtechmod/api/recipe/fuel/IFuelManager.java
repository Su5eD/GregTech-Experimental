package mods.gregtechmod.api.recipe.fuel;

import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;

import java.util.Collection;

public interface IFuelManager<T extends IFuel<? extends IRecipeIngredient, ?>, I> {
    boolean addFuel(T fuel);

    T getFuel(I target);

    boolean removeFuel(I target);

    Collection<T> getFuels();
}