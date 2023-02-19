package dev.su5ed.gtexperimental.recipe.type;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredientType;
import dev.su5ed.gtexperimental.api.recipe.RecipeOutputType;
import dev.su5ed.gtexperimental.api.recipe.RecipeProperty;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeOutputTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

import java.util.List;

public class SIMORecipeType<R extends SIMORecipe<T>, T> extends BaseRecipeTypeImpl<R> {
    protected final RecipeIngredientType<? extends RecipeIngredient<T>> inputType;
    protected final RecipeOutputType<T> outputType;
    protected final int outputCount;
    protected final List<RecipeProperty<?>> properties;
    protected final SIMORecipeFactory<R, T> factory;

    public SIMORecipeType(ResourceLocation name, RecipeIngredientType<? extends RecipeIngredient<T>> inputType, RecipeOutputType<T> outputType, int outputCount, List<RecipeProperty<?>> properties, SIMORecipeFactory<R, T> factory) {
        super(name);

        this.inputType = inputType;
        this.outputType = outputType;
        this.outputCount = outputCount;
        this.properties = properties;
        this.factory = factory;
    }

    public RecipeOutputType<T> getOutputType() {
        return this.outputType;
    }

    public int getOutputCount() {
        return this.outputCount;
    }

    public List<RecipeProperty<?>> getProperties() {
        return this.properties;
    }

    @Override
    public R fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
        JsonObject inputJson = GsonHelper.getAsJsonObject(serializedRecipe, "input");
        JsonArray outputJson = GsonHelper.getAsJsonArray(serializedRecipe, "output");

        RecipeIngredient<T> input = this.inputType.create(inputJson);
        List<T> outputs = RecipeUtil.parseOutputs(this.outputType, this.outputCount, outputJson);
        RecipePropertyMap properties = RecipePropertyMap.fromJson(recipeId, this.properties, serializedRecipe);

        return this.factory.create(recipeId, input, outputs, properties);
    }

    @Override
    public R fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        RecipeIngredient<T> input = this.inputType.create(buffer);
        List<T> outputs = ModRecipeOutputTypes.fromNetwork(this.outputType, this.outputCount, buffer);
        RecipePropertyMap properties = RecipePropertyMap.fromNetwork(this.properties, buffer);

        return this.factory.create(recipeId, input, outputs, properties);
    }

    public interface SIMORecipeFactory<R extends SIMORecipe<T>, T> {
        R create(ResourceLocation id, RecipeIngredient<T> input, List<T> outputs, RecipePropertyMap properties);
    }
}
