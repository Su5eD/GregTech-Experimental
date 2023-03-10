package dev.su5ed.gtexperimental.recipe.type;

import dev.su5ed.gtexperimental.api.recipe.BaseRecipe;
import net.minecraft.resources.ResourceLocation;

public interface BaseRecipeFactory<R extends BaseRecipe<?, ?, OUT, ? super R>, IN, OUT> {
    R create(ResourceLocation id, IN input, OUT output, RecipePropertyMap properties);
}
