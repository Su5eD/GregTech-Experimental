package dev.su5ed.gtexperimental.recipe.type;

import com.google.gson.JsonObject;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredientType;
import dev.su5ed.gtexperimental.api.recipe.RecipeOutputType;
import dev.su5ed.gtexperimental.api.recipe.RecipeProperty;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

import java.util.List;

public class SISORecipeType<R extends SISORecipe<IN, OUT>, IN, OUT> extends BaseRecipeTypeImpl<R> {
    public final RecipeIngredientType<? extends RecipeIngredient<IN>> inputType;
    public final RecipeOutputType<OUT> outputType;
    protected final List<RecipeProperty<?>> properties;
    protected final SISORecipeFactory<R, IN, OUT> factory;

    public SISORecipeType(ResourceLocation name, RecipeIngredientType<? extends RecipeIngredient<IN>> inputType, RecipeOutputType<OUT> outputType, List<RecipeProperty<?>> properties, SISORecipeFactory<R, IN, OUT> factory) {
        super(name);

        this.inputType = inputType;
        this.outputType = outputType;
        this.properties = properties;
        this.factory = factory;
    }

    public RecipeIngredientType<? extends RecipeIngredient<IN>> getInputType() {
        return this.inputType;
    }

    public RecipeOutputType<OUT> getOutputType() {
        return this.outputType;
    }

    public List<RecipeProperty<?>> getProperties() {
        return this.properties;
    }

    @Override
    public R fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
        JsonObject inputJson = GsonHelper.getAsJsonObject(serializedRecipe, "input");
        JsonObject outputJson = GsonHelper.getAsJsonObject(serializedRecipe, "output");

        RecipeIngredient<IN> input = this.inputType.create(inputJson);
        OUT output = this.outputType.fromJson(outputJson);
        RecipePropertyMap properties = RecipePropertyMap.fromJson(recipeId, this.properties, serializedRecipe);

        return this.factory.create(recipeId, input, output, properties);
    }

    @Override
    public R fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        RecipeIngredient<IN> input = this.inputType.create(buffer);
        OUT output = this.outputType.fromNetwork(buffer);
        RecipePropertyMap properties = RecipePropertyMap.fromNetwork(this.properties, buffer);

        return this.factory.create(recipeId, input, output, properties);
    }

    public interface SISORecipeFactory<R extends SISORecipe<IN, OUT>, IN, OUT> {
        R create(ResourceLocation id, RecipeIngredient<IN> input, OUT output, RecipePropertyMap properties);
    }
}
