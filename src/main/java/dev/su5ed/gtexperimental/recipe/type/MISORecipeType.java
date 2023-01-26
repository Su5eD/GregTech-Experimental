package dev.su5ed.gtexperimental.recipe.type;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredientType;
import dev.su5ed.gtexperimental.api.recipe.RecipeOutputType;
import dev.su5ed.gtexperimental.api.recipe.RecipeProperty;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

import java.util.Collections;
import java.util.List;

public class MISORecipeType<R extends MISORecipe<IN, OUT>, IN, OUT> extends BaseRecipeTypeImpl<R> {
    public final RecipeIngredientType<? extends RecipeIngredient<IN>> inputType;
    public final int inputCount;
    public final RecipeOutputType<OUT> outputType;
    public final List<RecipeProperty<?>> properties;
    private final MISORecipeFactory<R, IN, OUT> factory;

    public MISORecipeType(ResourceLocation name, RecipeIngredientType<? extends RecipeIngredient<IN>> inputType, int inputCount, RecipeOutputType<OUT> outputType, List<RecipeProperty<?>> properties, MISORecipeFactory<R, IN, OUT> factory) {
        super(name);

        this.inputType = inputType;
        this.inputCount = inputCount;
        this.outputType = outputType;
        this.properties = Collections.unmodifiableList(properties);
        this.factory = factory;
    }

    @Override
    public R fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
        JsonArray inputJson = GsonHelper.getAsJsonArray(serializedRecipe, "input");
        JsonElement outputJson = serializedRecipe.get("output");

        List<? extends RecipeIngredient<IN>> inputs = RecipeUtil.parseInputs(this.inputType, this.inputCount, inputJson);
        OUT output = this.outputType.fromJson(outputJson);
        RecipePropertyMap properties = RecipePropertyMap.fromJson(this.properties, serializedRecipe);

        return this.factory.create(recipeId, inputs, output, properties);
    }

    @Override
    public R fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        List<? extends RecipeIngredient<IN>> inputs = ModRecipeIngredientTypes.fromNetwork(this.inputType, this.inputCount, buffer);
        OUT output = this.outputType.fromNetwork(buffer);
        RecipePropertyMap properties = RecipePropertyMap.fromNetwork(this.properties, buffer);
        
        return this.factory.create(recipeId, inputs, output, properties);
    }

    public interface MISORecipeFactory<R extends MISORecipe<IN, OUT>, IN, OUT> {
        R create(ResourceLocation id, List<? extends RecipeIngredient<IN>> inputs, OUT output, RecipePropertyMap properties);
    }
}
