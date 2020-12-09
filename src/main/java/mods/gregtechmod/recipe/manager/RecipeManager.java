package mods.gregtechmod.recipe.manager;

import mods.gregtechmod.api.recipe.IGtMachineRecipe;
import mods.gregtechmod.api.recipe.IGtRecipeManager;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public abstract class RecipeManager<I, R extends IGtMachineRecipe<I, ?>, M> implements IGtRecipeManager<I, R> {
    protected final SortedSet<R> recipes;

    public RecipeManager(Comparator<R> comparator) {
        this.recipes = new TreeSet<>(comparator);
    }

    @Override
    public void addRecipe(R recipe) {
        this.recipes.add(recipe);
    }

    @Override
    public void removeRecipe(R recipe) {
        this.recipes.remove(recipe);
    }

    @Override
    public SortedSet<R> getRecipes() {
        return this.recipes;
    }
}
