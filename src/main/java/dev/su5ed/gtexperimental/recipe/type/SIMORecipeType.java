package dev.su5ed.gtexperimental.recipe.type;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredientType;
import dev.su5ed.gtexperimental.api.recipe.RecipeOutputType;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeOutputTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

import java.util.List;

public class SIMORecipeType<R extends SIMORecipe<T>, T> extends BaseRecipeTypeImpl<R> {
    protected final RecipeIngredientType<? extends RecipeIngredient<T>> inputType;
    protected final RecipeOutputType<T> outputType;
    protected final int outputCount;
    protected final SIMORecipeFactory<R, T> factory;

    public SIMORecipeType(ResourceLocation name, RecipeIngredientType<? extends RecipeIngredient<T>> inputType, RecipeOutputType<T> outputType, int outputCount, SIMORecipeFactory<R, T> factory) {
        super(name);

        this.inputType = inputType;
        this.outputType = outputType;
        this.outputCount = outputCount;
        this.factory = factory;
    }

    public RecipeOutputType<T> getOutputType() {
        return this.outputType;
    }

    public int getOutputCount() {
        return this.outputCount;
    }

    @Override
    public R fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
        JsonElement inputJson = GsonHelper.getAsJsonObject(serializedRecipe, "input");
        JsonArray outputJson = GsonHelper.getAsJsonArray(serializedRecipe, "output");

        RecipeIngredient<T> input = this.inputType.create(inputJson);
        List<T> outputs = RecipeUtil.parseOutputs(this.outputType, this.outputCount, outputJson);
        int duration = GsonHelper.getAsInt(serializedRecipe, "duration");
        double energyCost = GsonHelper.getAsDouble(serializedRecipe, "energyCost");

        return this.factory.create(recipeId, input, outputs, duration, energyCost);
    }

    @Override
    public R fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        RecipeIngredient<T> input = this.inputType.create(buffer);
        List<T> outputs = ModRecipeOutputTypes.fromNetwork(this.outputType, this.outputCount, buffer);
        int duration = buffer.readInt();
        double energyCost = buffer.readDouble();
        return this.factory.create(recipeId, input, outputs, duration, energyCost);
    }

    public interface SIMORecipeFactory<R extends SIMORecipe<T>, T> {
        R create(ResourceLocation id, RecipeIngredient<T> input, List<T> outputs, int duration, double energyCost);
    }
}
