package dev.su5ed.gtexperimental.api.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;

public interface BaseRecipeType<R extends BaseRecipe<?, ?, ?, OUT, ? super R>, RIN extends RecipeIngredientType<? extends IN, ?>, IN, OUT> extends RecipeType<R> {
    RIN getInputType();
    
    RecipeOutputType<OUT> getOutputType();
    
    List<RecipeProperty<?>> getProperties();
    
    R fromJson(ResourceLocation recipeId, JsonObject serializedRecipe);

    R fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer);
}
