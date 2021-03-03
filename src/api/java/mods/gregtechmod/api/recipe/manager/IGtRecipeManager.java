package mods.gregtechmod.api.recipe.manager;

import mods.gregtechmod.api.recipe.IMachineRecipe;

import java.util.Set;

public interface IGtRecipeManager<RI, I, R extends IMachineRecipe<RI, ?>> {
    default boolean addRecipe(R recipe) {
       return addRecipe(recipe, false);
    }

    boolean addRecipe(R recipe, boolean overwrite);

    /**
     * Checks if a recipe exists for the target input without checking the stacksize
     */
    boolean hasRecipeFor(I input);

    void removeRecipe(R recipe);

    Set<R> getRecipes();
}