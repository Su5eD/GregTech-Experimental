package dev.su5ed.gtexperimental.recipe;

import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeSerializers;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeTypes;
import dev.su5ed.gtexperimental.recipe.type.RecipePropertyMap;
import dev.su5ed.gtexperimental.recipe.type.SISORecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public class DenseLiquidFuel extends SISORecipe<FluidStack> {

    public DenseLiquidFuel(ResourceLocation id, RecipeIngredient<FluidStack> input, double energy) {
        this(id, input, FluidStack.EMPTY, RecipePropertyMap.builder()
            .energy(energy)
            .build());
    }

    public DenseLiquidFuel(ResourceLocation id, RecipeIngredient<FluidStack> input, FluidStack output, RecipePropertyMap properties) {
        super(ModRecipeTypes.DENSE_LIQUID_FUEL.get(), ModRecipeSerializers.DENSE_LIQUID_FUEL.get(), id, input, output, properties);
    }

    @Override
    protected boolean allowEmptyOutput() {
        return true;
    }
}
