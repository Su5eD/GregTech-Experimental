package mods.gregtechmod.api.recipe.fuel;

import java.util.Collection;

public interface IFuelManager<T extends IFuel<TI, I>, TI, I> {
    boolean addFuel(TI fuel, double energy);

    T getFuel(I target);

    boolean removeFuel(I target);

    Collection<T> getFuels();
}