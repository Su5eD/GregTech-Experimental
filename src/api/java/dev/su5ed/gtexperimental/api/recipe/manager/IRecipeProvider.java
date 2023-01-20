package dev.su5ed.gtexperimental.api.recipe.manager;

import dev.su5ed.gtexperimental.api.recipe.IMachineRecipe;

public interface IRecipeProvider<RI, I, R extends IMachineRecipe<RI, ?>> extends IRecipeHolder<I> {
    /**
     * Checks if a recipe exists which's input exactly matches the target input provided
     */
    R getRecipeFor(I input);
}
