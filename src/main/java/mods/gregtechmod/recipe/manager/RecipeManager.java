package mods.gregtechmod.recipe.manager;

import mods.gregtechmod.api.recipe.IGtMachineRecipe;
import mods.gregtechmod.api.recipe.IGtRecipeManager;

import java.util.ArrayList;
import java.util.List;

public abstract class RecipeManager<I, R extends IGtMachineRecipe<I, ?>> implements IGtRecipeManager<I, R> {
    protected final List<R> recipes = new ArrayList<>();

    @Override
    public void addRecipe(R recipe) {
        this.recipes.add(recipe);
    }

    @Override
    public void removeRecipe(R recipe) {
        this.recipes.remove(recipe);
    }

    @Override
    public List<R> getRecipes() {
        return this.recipes;
    }
}
