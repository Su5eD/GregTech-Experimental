package dev.su5ed.gtexperimental.recipe;

import dev.su5ed.gtexperimental.api.recipe.ListRecipeIngredientType;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.api.recipe.RecipeOutputType;
import dev.su5ed.gtexperimental.api.recipe.RecipeProperty;
import dev.su5ed.gtexperimental.recipe.type.BaseRecipeFactory;
import dev.su5ed.gtexperimental.recipe.type.BaseRecipeTypeImpl;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class MISORecipeType<R extends MISORecipe<IN, OUT>, IN, OUT> extends BaseRecipeTypeImpl<R, ListRecipeIngredientType<List<RecipeIngredient<IN>>, IN>, List<? extends RecipeIngredient<IN>>, OUT> {
    public MISORecipeType(ResourceLocation name, ListRecipeIngredientType<List<RecipeIngredient<IN>>, IN> inputType, RecipeOutputType<OUT> outputType, List<RecipeProperty<?>> properties, BaseRecipeFactory<R, List<? extends RecipeIngredient<IN>>, OUT> factory) {
        super(name, inputType, outputType, properties, factory);
    }
}
