package dev.su5ed.gtexperimental.recipe.crafting;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.function.BiFunction;

public class SimpleRecipeSerializer<T extends R, R extends Recipe<?>> implements RecipeSerializer<T> {
    private final RecipeSerializer<R> serializer;
    private final BiFunction<ResourceLocation, R, T> factory;

    public SimpleRecipeSerializer(RecipeSerializer<R> serializer, BiFunction<ResourceLocation, R, T> factory) {
        this.serializer = serializer;
        this.factory = factory;
    }

    @Override
    public T fromJson(ResourceLocation recipeId, JsonObject json) {
        R recipe = this.serializer.fromJson(recipeId, json);
        return this.factory.apply(recipeId, recipe);
    }

    @Override
    public T fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        R recipe = this.serializer.fromNetwork(recipeId, buffer);
        return this.factory.apply(recipeId, recipe);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, T recipe) {
        this.serializer.toNetwork(buffer, recipe);
    }
}
