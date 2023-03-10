package dev.su5ed.gtexperimental.recipe;

import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredientType;
import dev.su5ed.gtexperimental.api.recipe.RecipeOutputType;
import dev.su5ed.gtexperimental.api.recipe.RecipeProperty;
import dev.su5ed.gtexperimental.recipe.type.BaseRecipeFactory;
import dev.su5ed.gtexperimental.recipe.type.ModRecipeType;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class SIMORecipeType<R extends SIMORecipe<IN, OUT>, IN, OUT> extends ModRecipeType<R, RecipeIngredientType<? extends RecipeIngredient<IN>, IN>, RecipeIngredient<IN>, OUT> {
    public SIMORecipeType(ResourceLocation name, RecipeIngredientType<? extends RecipeIngredient<IN>, IN> inputType, RecipeOutputType<OUT> outputType, List<RecipeProperty<?>> properties, BaseRecipeFactory<R, RecipeIngredient<IN>, OUT> factory) {
        super(name, inputType, outputType, properties, factory);
    }
}
