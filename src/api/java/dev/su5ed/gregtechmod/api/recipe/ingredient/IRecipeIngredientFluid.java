package dev.su5ed.gregtechmod.api.recipe.ingredient;

import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IRecipeIngredientFluid extends IRecipeIngredient {
    boolean apply(@Nullable FluidStack fluidStack);

    boolean apply(Fluid fluid);

    int getMilliBuckets();

    List<Fluid> getMatchingFluids();
}
