package dev.su5ed.gregtechmod.recipe.type;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;

public interface BaseRecipeType<R extends BaseRecipe<?, ?, ? super R>> extends RecipeType<R> {
    R fromJson(ResourceLocation recipeId, JsonObject serializedRecipe);

    R fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer);
}
