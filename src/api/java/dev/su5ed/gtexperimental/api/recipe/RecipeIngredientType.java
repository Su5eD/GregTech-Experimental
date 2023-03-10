package dev.su5ed.gtexperimental.api.recipe;

import com.google.gson.JsonElement;
import net.minecraft.network.FriendlyByteBuf;

import java.util.List;

public interface RecipeIngredientType<T, U> {
    T create(JsonElement json);

    T create(FriendlyByteBuf buffer);
    
    ListRecipeIngredientType<List<T>, U> listOf(int count);
}
