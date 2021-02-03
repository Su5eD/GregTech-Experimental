package mods.gregtechmod.recipe.manager;

import mods.gregtechmod.api.recipe.IGtMachineRecipe;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManager;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public abstract class RecipeManager<RI, I, R extends IGtMachineRecipe<RI, ?>> implements IGtRecipeManager<RI, I, R> {
    protected final SortedSet<R> recipes;

    public RecipeManager(Comparator<R> comparator) {
        this.recipes = new TreeSet<>(comparator);
    }

    @Override
    public boolean addRecipe(R recipe) {
        return addRecipe(recipe, false);
    }

    @Override
    public boolean addRecipe(R recipe, boolean overwrite) {
        if (recipe.isInvalid()) return false;
        if (overwrite) this.recipes.remove(recipe);

        return this.recipes.add(recipe);
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
