package dev.su5ed.gtexperimental.api.recipe;

import java.util.function.Function;

public interface CompositeRecipeIngredient<T> extends RecipeIngredient<T> {
    <U> RecipeIngredient<U> getSubIngredient(String name);
    
    record SubIngredient<T, U>(String name, RecipeIngredient<U> ingredient, Function<T, U> valueFunction) {}
}
