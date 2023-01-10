package dev.su5ed.gregtechmod.recipe.type;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;

public class BaseRecipeSerializer<T extends BaseRecipe<?>> implements RecipeSerializer<T> {
    private final BaseRecipeType<T> recipeType;

    public BaseRecipeSerializer(BaseRecipeType<T> recipeType) {
        this.recipeType = recipeType;
    }

    @Override
    public T fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
        return this.recipeType.fromJson(recipeId, serializedRecipe);
    }

    @Nullable
    @Override
    public T fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        return this.recipeType.fromNetwork(recipeId, buffer);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, T recipe) {
        recipe.toNetwork(buffer);
    }
}
