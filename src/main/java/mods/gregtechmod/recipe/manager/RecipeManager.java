package mods.gregtechmod.recipe.manager;

import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManager;
import mods.gregtechmod.api.recipe.manager.IRecipeProvider;

import java.util.*;

public abstract class RecipeManager<RI, I, R extends IMachineRecipe<RI, ?>> implements IGtRecipeManager<RI, I, R> {
    protected final List<R> recipes = new ArrayList<>();
    private final Collection<IRecipeProvider<RI, I, R>> providers = new HashSet<>();

    @Override
    public boolean addRecipe(R recipe, boolean overwrite) {
        if (recipe.isInvalid()) return false;

        R existing = getRecipeForExact(recipe);
        if (existing != null) {
            if (overwrite) this.recipes.remove(existing);
            else return false;
        }

        return this.recipes.add(recipe);
    }

    @Override
    public void removeRecipe(R recipe) {
        if (recipe != null) this.recipes.remove(recipe);
    }

    @Override
    public List<R> getRecipes() {
        return this.recipes;
    }

    protected abstract R getRecipeForExact(R recipe);

    protected R getProvidedRecipe(I input) {
        return this.providers.stream()
            .map(provider -> provider.getRecipeFor(input))
            .filter(Objects::nonNull)
            .findFirst()
            .orElse(null);
    }

    @Override
    public void registerProvider(IRecipeProvider<RI, I, R> provider) {
        this.providers.add(provider);
    }
}
