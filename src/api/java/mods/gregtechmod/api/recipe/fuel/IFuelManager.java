package mods.gregtechmod.api.recipe.fuel;

import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;

import java.util.Collection;

public interface IFuelManager<T extends IFuel<? extends IRecipeIngredient>, I> extends IFuelProvider<T, I> {
    boolean addFuel(T fuel);
    
    boolean removeFuel(I target);

    Collection<T> getFuels();
    
    void registerProvider(IFuelProvider<T, I> provider);
}
