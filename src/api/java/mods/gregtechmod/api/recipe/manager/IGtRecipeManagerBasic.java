package mods.gregtechmod.api.recipe.manager;

import mods.gregtechmod.api.recipe.IMachineRecipe;

public interface IGtRecipeManagerBasic<RI, I, R extends IMachineRecipe<RI, ?>> extends IGtRecipeManager<RI, I, R> {
    /**
     * Checks if a recipe exists which's input exactly matches the target input provided
     */
    R getRecipeFor(I input);
}
