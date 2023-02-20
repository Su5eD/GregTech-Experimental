package dev.su5ed.gtexperimental.recipe;

import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeSerializers;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeTypes;
import dev.su5ed.gtexperimental.recipe.type.RecipePropertyMap;
import dev.su5ed.gtexperimental.recipe.type.SIMORecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class DistillationRecipe extends SIMORecipe<FluidStack, FluidStack> {

    public DistillationRecipe(ResourceLocation id, RecipeIngredient<FluidStack> input, List<FluidStack> outputs, int duration) {
        this(id, input, outputs, RecipePropertyMap.builder()
            .duration(duration)
            .build());
    }

    public DistillationRecipe(ResourceLocation id, RecipeIngredient<FluidStack> input, List<FluidStack> outputs, RecipePropertyMap properties) {
        super(ModRecipeTypes.DISTILLATION.get(), ModRecipeSerializers.DISTILLATION.get(), id, input, outputs, properties);
    }

    @Override
    public double getEnergyCost() {
        return 16;
    }
}
