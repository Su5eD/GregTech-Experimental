package dev.su5ed.gtexperimental.recipe.type;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredientType;
import dev.su5ed.gtexperimental.api.recipe.RecipeOutputType;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

import java.util.List;

public class MISORecipeType<R extends MISORecipe<T>, T> extends BaseRecipeTypeImpl<R> {
    public final RecipeIngredientType<? extends RecipeIngredient<T>> inputType;
    public final int inputCount;
    public final RecipeOutputType<T> outputType;
    private final MISORecipeFactory<R, T> factory;

    public MISORecipeType(ResourceLocation name, RecipeIngredientType<? extends RecipeIngredient<T>> inputType, int inputCount, RecipeOutputType<T> outputType, MISORecipeFactory<R, T> factory) {
        super(name);

        this.inputType = inputType;
        this.inputCount = inputCount;
        this.outputType = outputType;
        this.factory = factory;
    }

    @Override
    public R fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
        JsonArray inputJson = GsonHelper.getAsJsonArray(serializedRecipe, "input");
        JsonElement outputJson = serializedRecipe.get("output");

        List<? extends RecipeIngredient<T>> inputs = RecipeUtil.parseInputs(this.inputType, this.inputCount, inputJson);
        T output = this.outputType.fromJson(outputJson);
        int duration = GsonHelper.getAsInt(serializedRecipe, "duration");
        double energyCost = GsonHelper.getAsDouble(serializedRecipe, "energyCost");

        return this.factory.create(recipeId, inputs, output, duration, energyCost);
    }

    @Override
    public R fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        List<? extends RecipeIngredient<T>> inputs = ModRecipeIngredientTypes.fromNetwork(this.inputType, this.inputCount, buffer);
        T output = this.outputType.fromNetwork(buffer);
        int duration = buffer.readInt();
        double energyCost = buffer.readDouble();
        return this.factory.create(recipeId, inputs, output, duration, energyCost);
    }

    public interface MISORecipeFactory<R extends MISORecipe<T>, T> {
        R create(ResourceLocation id, List<? extends RecipeIngredient<T>> inputs, T output, int duration, double energyCost);
    }
}
