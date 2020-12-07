package mods.gregtechmod.api.recipe;

import java.util.List;

public interface IGtRecipeManager<I, R extends IGtMachineRecipe<I, ?>> {
    void addRecipe(R recipe);

    R getRecipeFor(I input);

    void removeRecipe(R recipe);

    List<R> getRecipes();
}