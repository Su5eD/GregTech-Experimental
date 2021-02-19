package mods.gregtechmod.recipe.fuel;

import mods.gregtechmod.api.recipe.fuel.IFuel;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;

public abstract class Fuel<T extends IRecipeIngredient, I> implements IFuel<T, I> {
    protected final T fuel;
    protected final double energy;

    public Fuel(T fuel, double energy) {
        this.fuel = fuel;
        this.energy = energy;
    }

    @Override
    public T getFuel() {
        return this.fuel;
    }

    @Override
    public double getEnergy() {
        return this.energy;
    }
}
