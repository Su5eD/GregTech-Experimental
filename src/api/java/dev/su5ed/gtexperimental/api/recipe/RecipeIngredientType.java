package dev.su5ed.gtexperimental.api.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;

public interface RecipeIngredientType<T extends RecipeIngredient<?>> {
    T create(JsonObject json);

    T create(FriendlyByteBuf buffer);
}
