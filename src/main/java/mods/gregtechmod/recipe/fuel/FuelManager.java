package mods.gregtechmod.recipe.fuel;

import mods.gregtechmod.api.recipe.fuel.IFuel;
import mods.gregtechmod.api.recipe.fuel.IFuelManager;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class FuelManager<F extends IFuel<FI, ?>, FI extends IRecipeIngredient, I> implements IFuelManager<F, I> {
    protected final Set<F> fuels = new HashSet<>();

    @Override
    public boolean addFuel(F fuel) {
        if (fuel.isInvalid()) return false;
        return this.fuels.add(fuel);
    }

    @Override
    public boolean removeFuel(I target) {
        F fuel = this.getFuel(target);
        return fuel != null && this.fuels.remove(fuel);
    }

    @Override
    public Collection<F> getFuels() {
        return this.fuels;
    }
}
