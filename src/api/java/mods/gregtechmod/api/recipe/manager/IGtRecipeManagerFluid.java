package mods.gregtechmod.api.recipe.manager;

import mods.gregtechmod.api.recipe.IMachineRecipe;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

public interface IGtRecipeManagerFluid<RI, I, R extends IMachineRecipe<RI, ?>> extends IGtRecipeManager<RI, I, R> {
    R getRecipeFor(@Nullable FluidStack input);

    boolean hasRecipeFor(FluidStack input);

    boolean hasRecipeFor(Fluid input);
}
