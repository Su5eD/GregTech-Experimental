package dev.su5ed.gtexperimental.api.recipe;

import com.google.gson.JsonElement;
import net.minecraft.network.FriendlyByteBuf;

public interface RecipeIngredientType<T extends RecipeIngredient<?>> {
    T create(JsonElement json);

    T create(FriendlyByteBuf buffer);
}
