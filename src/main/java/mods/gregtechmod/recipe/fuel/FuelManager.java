package mods.gregtechmod.recipe.fuel;

import mods.gregtechmod.api.recipe.fuel.IFuel;
import mods.gregtechmod.api.recipe.fuel.IFuelManager;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class FuelManager<FI extends IRecipeIngredient, I> implements IFuelManager<IFuel<FI, I>, FI, I> {
    protected final Set<IFuel<FI, I>> fuels = new HashSet<>();

    @Override
    public boolean addFuel(IFuel<FI, I> fuel) {
        if (fuel.isInvalid()) return false;
        return this.fuels.add(fuel);
    }

    @Override
    public boolean removeFuel(I target) {
        IFuel<FI, I> fuel = this.getFuel(target);
        return fuel != null && this.fuels.remove(fuel);
    }

    @Override
    public Collection<IFuel<FI, I>> getFuels() {
        return this.fuels;
    }
}
