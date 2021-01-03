package mods.gregtechmod.api.recipe.ingredient;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.List;

public interface IRecipeIngredientFluid extends IRecipeIngredient {
    boolean apply(@Nullable FluidStack fluidStack);

    boolean apply(Fluid fluid);

    int getMilliBuckets();

    List<Fluid> getMatchingFluids();
}
