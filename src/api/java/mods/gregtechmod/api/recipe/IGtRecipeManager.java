package mods.gregtechmod.api.recipe;

import java.util.SortedSet;

public interface IGtRecipeManager<RI, I, R extends IGtMachineRecipe<RI, ?>> {
    void addRecipe(R recipe);

    /**
     * Checks if a recipe exists which's input exactly matches the target input provided
     */
    R getRecipeFor(I input);

    /**
     * Checks if a recipe exists for the target input without checking the stacksize
     */
    boolean hasRecipeFor(I input);

    void removeRecipe(R recipe);

    SortedSet<R> getRecipes();
}