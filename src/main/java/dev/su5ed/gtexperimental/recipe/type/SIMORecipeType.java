package dev.su5ed.gtexperimental.recipe.type;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredientType;
import dev.su5ed.gtexperimental.api.recipe.RecipeOutputType;
import dev.su5ed.gtexperimental.api.recipe.RecipeProperty;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

import java.util.List;

public class SIMORecipeType<R extends SIMORecipe<IN, OUT>, IN, OUT> extends BaseRecipeTypeImpl<R, OUT> {
    protected final RecipeIngredientType<? extends RecipeIngredient<IN>> inputType;
    protected final SIMORecipeFactory<R, IN, OUT> factory;

    public SIMORecipeType(ResourceLocation name, RecipeIngredientType<? extends RecipeIngredient<IN>> inputType, RecipeOutputType<OUT> outputType, List<RecipeProperty<?>> properties, SIMORecipeFactory<R, IN, OUT> factory) {
        super(name, outputType, properties);

        this.inputType = inputType;
        this.factory = factory;
    }

    @Override
    public R fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
        JsonObject inputJson = GsonHelper.getAsJsonObject(serializedRecipe, "input");
        JsonArray outputJson = GsonHelper.getAsJsonArray(serializedRecipe, "output");

        RecipeIngredient<IN> input = this.inputType.create(inputJson);
        OUT outputs = this.outputType.fromJson(outputJson);
        RecipePropertyMap properties = RecipePropertyMap.fromJson(recipeId, this.properties, serializedRecipe);

        return this.factory.create(recipeId, input, outputs, properties);
    }

    @Override
    public R fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        RecipeIngredient<IN> input = this.inputType.create(buffer);
        OUT outputs = this.outputType.fromNetwork(buffer);
        RecipePropertyMap properties = RecipePropertyMap.fromNetwork(this.properties, buffer);

        return this.factory.create(recipeId, input, outputs, properties);
    }

    public interface SIMORecipeFactory<R extends SIMORecipe<IN, OUT>, IN, OUT> {
        R create(ResourceLocation id, RecipeIngredient<IN> input, OUT outputs, RecipePropertyMap properties);
    }
}
