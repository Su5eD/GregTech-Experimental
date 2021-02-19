package mods.gregtechmod.recipe.fuel;

import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import net.minecraftforge.fluids.Fluid;

public class FuelFluid extends Fuel<IRecipeIngredientFluid, Fluid> {

    public FuelFluid(IRecipeIngredientFluid fuel, double energy) {
        super(fuel, energy);
    }

    @Override
    public boolean apply(Fluid fuel) {
        return this.fuel.apply(fuel);
    }
}
