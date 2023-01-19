package dev.su5ed.gregtechmod.recipe.type;

import java.util.List;

public interface RecipeManager<R extends BaseRecipe<?, I, ? super R>, I> extends RecipeProvider<R, I> {
    List<R> getRecipes();
    
    void registerProvider(RecipeProvider<R, I> provider);
}
