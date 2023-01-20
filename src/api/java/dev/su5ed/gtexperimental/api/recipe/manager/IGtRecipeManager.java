package dev.su5ed.gtexperimental.api.recipe.manager;

import dev.su5ed.gtexperimental.api.recipe.IMachineRecipe;

import java.util.List;

public interface IGtRecipeManager<RI, I, R extends IMachineRecipe<RI, ?>> extends IRecipeHolder<I> {
    default boolean addRecipe(R recipe) {
       return addRecipe(recipe, false);
    }

    boolean addRecipe(R recipe, boolean overwrite);

    void removeRecipe(R recipe);

    List<R> getRecipes();
    
    void registerProvider(IRecipeProvider<RI, I, R> provider);
}
