package mods.gregtechmod.api.recipe;

import java.util.List;
import java.util.Map;

public interface IGtRecipeManager<I, R extends IGtMachineRecipe<I, ?>, M> {
    void addRecipe(R recipe);

    R getRecipeFor(I input);

    R getRecipeFor(I input, Map<String, M> metadata);

    void removeRecipe(R recipe);

    List<R> getRecipes();
}