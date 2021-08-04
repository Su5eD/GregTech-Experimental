package mods.gregtechmod.recipe.fuel;

import mods.gregtechmod.api.recipe.fuel.IFuel;
import mods.gregtechmod.api.recipe.fuel.IFuelManager;
import mods.gregtechmod.api.recipe.fuel.IFuelProvider;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public abstract class FuelManager<F extends IFuel<? extends IRecipeIngredient>, I> implements IFuelManager<F, I> {
    protected final Set<F> fuels = new HashSet<>();
    private final Collection<IFuelProvider<F, I>> providers = new HashSet<>();

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
    
    protected F getProvidedFuel(I target) {
        return this.providers.stream()
                .map(provider -> provider.getFuel(target))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }
    
    protected boolean hasProvidedFuel(I target) {
        return this.providers.stream()
                .anyMatch(provider -> provider.hasFuel(target));
    }

    @Override
    public void registerProvider(IFuelProvider<F, I> provider) {
        this.providers.add(provider);
    }
}
