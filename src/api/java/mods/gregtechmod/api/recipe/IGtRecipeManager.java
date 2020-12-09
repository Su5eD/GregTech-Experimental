package mods.gregtechmod.api.recipe;

import java.util.SortedSet;

public interface IGtRecipeManager<I, R extends IGtMachineRecipe<I, ?>> {
    void addRecipe(R recipe);

    R getRecipeFor(I input);

    void removeRecipe(R recipe);

    SortedSet<R> getRecipes();
}