package dev.su5ed.gtexperimental.api.recipe;

import java.util.List;

public interface ListRecipeIngredient<T> extends RecipeIngredient<List<T>> {
    RecipeIngredient<T> get(int index);
}
