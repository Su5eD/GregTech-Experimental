package mods.gregtechmod.api.recipe;

import java.util.Map;
import java.util.SortedSet;

public interface IGtRecipeManager<I, R extends IGtMachineRecipe<I, ?>, M> {
    void addRecipe(R recipe);

    R getRecipeFor(I input);

    R getRecipeFor(I input, Map<String, M> metadata);

    void removeRecipe(R recipe);

    SortedSet<R> getRecipes();
}