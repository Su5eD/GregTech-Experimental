package mods.gregtechmod.recipe.fuel;

import mods.gregtechmod.api.recipe.fuel.IFuel;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import net.minecraftforge.fluids.Fluid;

public class FluidFuelManager extends FuelManager<IRecipeIngredientFluid, Fluid> {

    @Override
    public IFuel<IRecipeIngredientFluid, Fluid> getFuel(Fluid target) {
        return this.fuels.stream()
                .filter(fuel -> fuel.getInput().apply(target))
                .findFirst()
                .orElse(null);
    }
}
