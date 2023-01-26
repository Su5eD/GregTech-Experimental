package dev.su5ed.gtexperimental.recipe;

import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeSerializers;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeTypes;
import dev.su5ed.gtexperimental.recipe.type.BaseRecipeSerializer;
import dev.su5ed.gtexperimental.recipe.type.SIMORecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class DistillationRecipe extends SIMORecipe<FluidStack> {

    public DistillationRecipe(ResourceLocation id, RecipeIngredient<FluidStack> input, List<FluidStack> outputs, int duration, double energyCost) {
        super(ModRecipeTypes.DISTILLATION.get(), ModRecipeSerializers.DISTILLATION.get(), id, input, outputs, duration, 16);
    }

    public static BaseRecipeSerializer<DistillationRecipe> createSerializer() {
        return new BaseRecipeSerializer<>(ModRecipeTypes.DISTILLATION.get());
    }
}
