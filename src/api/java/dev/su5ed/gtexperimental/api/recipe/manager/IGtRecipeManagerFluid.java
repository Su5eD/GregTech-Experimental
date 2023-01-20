package dev.su5ed.gtexperimental.api.recipe.manager;

import dev.su5ed.gtexperimental.api.recipe.IMachineRecipe;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

public interface IGtRecipeManagerFluid<RI, I, R extends IMachineRecipe<RI, ?>> extends IGtRecipeManager<RI, I, R> {
    R getRecipeFor(@Nullable FluidStack input);

    boolean hasRecipeFor(FluidStack input);

    boolean hasRecipeFor(Fluid input);
}
