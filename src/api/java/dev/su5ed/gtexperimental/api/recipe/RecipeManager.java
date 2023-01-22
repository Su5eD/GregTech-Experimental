package dev.su5ed.gtexperimental.api.recipe;

import java.util.List;

public interface RecipeManager<R extends BaseRecipe<?, I, ? super R>, I> extends RecipeProvider<R, I> {
    List<R> getRecipes();
}
