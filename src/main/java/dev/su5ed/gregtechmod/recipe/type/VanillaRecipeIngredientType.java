package dev.su5ed.gregtechmod.recipe.type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;

public class VanillaRecipeIngredientType implements RecipeIngredientType<VanillaRecipeIngredient> {
    @Override
    public VanillaRecipeIngredient create(JsonElement json) {
        JsonObject obj = json.getAsJsonObject();
        JsonObject value = obj.getAsJsonObject("value");
        int count = GsonHelper.getAsInt(obj, "count");
        return new VanillaRecipeIngredient(Ingredient.fromJson(value), count);
    }

    @Override
    public VanillaRecipeIngredient create(FriendlyByteBuf buffer) {
        return new VanillaRecipeIngredient(Ingredient.fromNetwork(buffer));
    }
}
