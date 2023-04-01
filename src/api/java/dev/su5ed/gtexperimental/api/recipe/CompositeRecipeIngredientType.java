package dev.su5ed.gtexperimental.api.recipe;

import java.util.Map;
import java.util.function.Function;

public interface CompositeRecipeIngredientType<T> extends RecipeIngredientType<CompositeRecipeIngredient<T>, T> {
    CompositeRecipeIngredient<T> createIngredient(Map<String, ? extends RecipeIngredient<?>> subIngredients);

    record SubIngredientType<T, U>(RecipeIngredientType<? extends RecipeIngredient<?>, U> type, Function<T, U> testValueSupplier) {
        public CompositeRecipeIngredient.SubIngredient<T, U> toIngredient(String name, RecipeIngredient<U> ingredient) {
            return new CompositeRecipeIngredient.SubIngredient<>(name, ingredient, this.testValueSupplier);
        }
    }
}
