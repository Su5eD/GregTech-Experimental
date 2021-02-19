package mods.gregtechmod.api.recipe.manager;

import mods.gregtechmod.api.recipe.IGtMachineRecipe;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public interface IGtRecipeManagerFluid<RI, I, R extends IGtMachineRecipe<RI, ?>> extends IGtRecipeManager<RI, I, R> {
    R getRecipeFor(FluidStack input);

    boolean hasRecipeFor(FluidStack input);

    boolean hasRecipeFor(Fluid input);
}
