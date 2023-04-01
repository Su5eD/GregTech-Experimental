package dev.su5ed.gtexperimental.recipe.type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.su5ed.gtexperimental.api.recipe.BaseRecipe;
import dev.su5ed.gtexperimental.api.recipe.BaseRecipeType;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredientType;
import dev.su5ed.gtexperimental.api.recipe.RecipeOutputType;
import dev.su5ed.gtexperimental.api.recipe.RecipeProperty;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.List;

public abstract class BaseRecipeTypeImpl<R extends BaseRecipe<?, ?, ?, OUT, ? super R>, RIN extends RecipeIngredientType<? extends IN, ?>, IN, OUT> implements BaseRecipeType<R, RIN, IN, OUT> {
    private final ResourceLocation name;
    protected final RIN inputType;
    protected final RecipeOutputType<OUT> outputType;
    protected final List<RecipeProperty<?>> properties;
    protected final BaseRecipeFactory<R, IN, OUT> factory;

    public BaseRecipeTypeImpl(ResourceLocation name, RIN inputType, RecipeOutputType<OUT> outputType, List<RecipeProperty<?>> properties, BaseRecipeFactory<R, IN, OUT> factory) {
        this.name = name;
        this.inputType = inputType;
        this.outputType = outputType;
        this.properties = Collections.unmodifiableList(properties);
        this.factory = factory;
    }

    @Override
    public RIN getInputType() {
        return this.inputType;
    }

    @Override
    public RecipeOutputType<OUT> getOutputType() {
        return this.outputType;
    }

    @Override
    public List<RecipeProperty<?>> getProperties() {
        return this.properties;
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

    @Override
    public String toString() {
        return this.name.toString();
    }
}
