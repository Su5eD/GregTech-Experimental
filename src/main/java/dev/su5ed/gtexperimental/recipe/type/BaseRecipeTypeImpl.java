package dev.su5ed.gtexperimental.recipe.type;

import dev.su5ed.gtexperimental.api.recipe.BaseRecipe;
import dev.su5ed.gtexperimental.api.recipe.BaseRecipeType;
import dev.su5ed.gtexperimental.api.recipe.RecipeOutputType;
import dev.su5ed.gtexperimental.api.recipe.RecipeProperty;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.List;

public abstract class BaseRecipeTypeImpl<R extends BaseRecipe<?, ?, OUT, ? super R>, OUT> implements BaseRecipeType<R, OUT> {
    private final ResourceLocation name;
    protected final RecipeOutputType<OUT> outputType;
    protected final List<RecipeProperty<?>> properties;

    public BaseRecipeTypeImpl(ResourceLocation name, RecipeOutputType<OUT> outputType, List<RecipeProperty<?>> properties) {
        this.name = name;
        this.outputType = outputType;
        this.properties = Collections.unmodifiableList(properties);
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
    public String toString() {
        return this.name.toString();
    }
}
