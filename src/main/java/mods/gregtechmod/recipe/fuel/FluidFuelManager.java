package mods.gregtechmod.recipe.fuel;

import mods.gregtechmod.api.recipe.fuel.IFuel;
import mods.gregtechmod.api.recipe.fuel.IFuelManager;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import net.minecraftforge.fluids.Fluid;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class FluidFuelManager implements IFuelManager<IFuel<IRecipeIngredientFluid, Fluid>, IRecipeIngredientFluid, Fluid> {
    private final Set<IFuel<IRecipeIngredientFluid, Fluid>> fuels = new HashSet<>();

    @Override
    public boolean addFuel(IRecipeIngredientFluid fuel, double energy) {
        return this.fuels.add(new FuelFluid(fuel, energy));
    }

    @Override
    public IFuel<IRecipeIngredientFluid, Fluid> getFuel(Fluid target) {
        return this.fuels.stream()
                .filter(fuel -> fuel.getFuel().apply(target))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean removeFuel(Fluid target) {
        IFuel<IRecipeIngredientFluid, Fluid> fuel = this.getFuel(target);
        return fuel != null && this.fuels.remove(fuel);
    }

    @Override
    public Collection<IFuel<IRecipeIngredientFluid, Fluid>> getFuels() {
        return this.fuels;
    }
}
