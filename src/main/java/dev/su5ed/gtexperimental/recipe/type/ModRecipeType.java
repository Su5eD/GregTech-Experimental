package dev.su5ed.gtexperimental.recipe.type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.su5ed.gtexperimental.api.recipe.BaseRecipe;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredientType;
import dev.su5ed.gtexperimental.api.recipe.RecipeOutputType;
import dev.su5ed.gtexperimental.api.recipe.RecipeProperty;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class ModRecipeType<R extends BaseRecipe<?, ?, OUT, ? super R>, RIN extends RecipeIngredientType<? extends IN, ?>, IN, OUT> extends BaseRecipeTypeImpl<R, OUT> {
    protected final RIN inputType;
    protected final BaseRecipeFactory<R, IN, OUT> factory;

    public ModRecipeType(ResourceLocation name, RIN inputType, RecipeOutputType<OUT> outputType, List<RecipeProperty<?>> properties, BaseRecipeFactory<R, IN, OUT> factory) {
        super(name, outputType, properties);

        this.inputType = inputType;
        this.factory = factory;
    }

    public RIN getInputType() {
        return inputType;
    }

    @Override
    public R fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
        JsonElement inputJson = serializedRecipe.get("input");
        JsonElement outputJson = serializedRecipe.get("output");

        IN input = this.inputType.create(inputJson);
        OUT output = this.outputType.fromJson(outputJson);
        RecipePropertyMap properties = RecipePropertyMap.fromJson(recipeId, this.properties, serializedRecipe);

        return this.factory.create(recipeId, input, output, properties);
    }

    @Override
    public R fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        IN input = this.inputType.create(buffer);
        OUT output = this.outputType.fromNetwork(buffer);
        RecipePropertyMap properties = RecipePropertyMap.fromNetwork(this.properties, buffer);

        return this.factory.create(recipeId, input, output, properties);
    }
}
