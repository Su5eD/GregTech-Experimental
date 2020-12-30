package mods.gregtechmod.api.recipe.ingredient;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public interface IRecipeIngredientFluid extends IRecipeIngredient {
    boolean apply(FluidStack fluidStack);

    int getFluidAmount();

    List<Fluid> getMatchingFluids();
}
