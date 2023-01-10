package dev.su5ed.gregtechmod.recipe.type;

import com.google.gson.JsonElement;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.crafting.Ingredient;

public class VanillaRecipeIngredientType implements RecipeIngredientType<VanillaRecipeIngredient> {
    @Override
    public VanillaRecipeIngredient create(JsonElement json) {
        return new VanillaRecipeIngredient(Ingredient.fromJson(json));
    }

    @Override
    public VanillaRecipeIngredient create(FriendlyByteBuf buffer) {
        return new VanillaRecipeIngredient(Ingredient.fromNetwork(buffer));
    }
}
