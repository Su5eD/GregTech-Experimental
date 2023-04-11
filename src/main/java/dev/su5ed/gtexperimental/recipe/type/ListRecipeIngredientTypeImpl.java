package dev.su5ed.gtexperimental.recipe.type;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import dev.su5ed.gtexperimental.api.recipe.ListRecipeIngredient;
import dev.su5ed.gtexperimental.api.recipe.ListRecipeIngredientType;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredientType;
import net.minecraft.network.FriendlyByteBuf;
import one.util.streamex.StreamEx;

import java.util.List;

public class ListRecipeIngredientTypeImpl<T, U> implements ListRecipeIngredientType<List<T>, U> {
    private final RecipeIngredientType<T, U> ingredientType;
    private final int count;

    public ListRecipeIngredientTypeImpl(RecipeIngredientType<T, U> ingredientType, int count) {
        this.ingredientType = ingredientType;
        this.count = count;
    }

    @Override
    public ListRecipeIngredient<U> createIngredient(List<? extends RecipeIngredient<U>> recipeIngredients) {
        return new ListRecipeIngredientImpl<>(this, recipeIngredients);
    }

    @Override
    public List<T> create(JsonElement json) {
        JsonArray array = json.getAsJsonArray();
        if (!array.isEmpty() && this.count >= array.size()) {
            return StreamEx.of(json.getAsJsonArray().iterator())
                .map(this.ingredientType::create)
                .toList();
        }
        throw new IllegalArgumentException("Ingredient type and json sizes differ");
    }

    @Override
    public List<T> create(FriendlyByteBuf buffer) {
        List<T> list = buffer.readList(this.ingredientType::create);
        if (list.size() > this.count) {
            throw new IllegalArgumentException("There are more inputs than known input types");
        }
        return list;
    }

    @Override
    public ListRecipeIngredientType<List<List<T>>, List<U>> listOf(int count) {
        return new ListRecipeIngredientTypeImpl<>(this, count);
    }

    @Override
    public int getIngredientCount() {
        return this.count;
    }
}
