package dev.su5ed.gregtechmod.recipe.type;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;

public interface BaseRecipeType<T extends BaseRecipe<?>> extends RecipeType<T> {
    T fromJson(ResourceLocation recipeId, JsonObject serializedRecipe);

    T fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer);
}
