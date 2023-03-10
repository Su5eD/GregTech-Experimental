package dev.su5ed.gtexperimental.api.recipe;

import java.util.List;

public interface ListRecipeIngredientType<T, U> extends RecipeIngredientType<T, List<U>> {
    int getIngredientCount();

    ListRecipeIngredient<U> createIngredient(List<? extends RecipeIngredient<U>> ingredients);
}
