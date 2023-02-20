package dev.su5ed.gtexperimental.recipe.gen;

import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.recipe.DenseLiquidFuel;
import dev.su5ed.gtexperimental.recipe.type.SISORecipe;
import net.minecraftforge.fluids.FluidStack;

public final class ModFuelBuilders {

    public static SISORecipeBuilder<FluidStack> denseLiquid(RecipeIngredient<FluidStack> input, double energy) {
        SISORecipe<FluidStack> recipe = new DenseLiquidFuel(null, input, energy);
        return new SISORecipeBuilder<>(recipe);
    }

    private ModFuelBuilders() {}
}
