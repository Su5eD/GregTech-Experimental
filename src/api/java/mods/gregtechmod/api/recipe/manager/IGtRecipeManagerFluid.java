package mods.gregtechmod.api.recipe.manager;

import mods.gregtechmod.api.recipe.IGtMachineRecipe;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public interface IGtRecipeManagerFluid<RI, I, R extends IGtMachineRecipe<RI, ?>> extends IGtRecipeManager<RI, I, R> {
    default R getRecipeFor(FluidStack input) {
        return getRecipeFor(input, -1);
    }

    R getRecipeFor(FluidStack input, int cells);

    boolean hasRecipeFor(FluidStack fluid);

    boolean hasRecipeFor(Fluid input);
}
