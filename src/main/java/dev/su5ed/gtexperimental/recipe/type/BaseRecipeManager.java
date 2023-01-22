package dev.su5ed.gtexperimental.recipe.type;

import dev.su5ed.gtexperimental.api.recipe.BaseRecipe;
import dev.su5ed.gtexperimental.api.recipe.RecipeManager;
import dev.su5ed.gtexperimental.api.recipe.RecipeProvider;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import one.util.streamex.StreamEx;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class BaseRecipeManager<R extends BaseRecipe<?, I, ? super R>, I> implements RecipeManager<R, I> {
    private final List<R> recipes;
    private final Collection<RecipeProvider<R, I>> providers;

    public BaseRecipeManager(RecipeType<R> recipeType, Level level, Collection<RecipeProvider<R, I>> providers) {
        this.recipes = level.getRecipeManager().getAllRecipesFor(recipeType);
        this.providers = providers;
    }

    public List<R> getRecipes() {
        return this.recipes;
    }

    @Override
    public boolean hasRecipeFor(I input) {
        return StreamEx.of(this.recipes)
            .anyMatch(r -> r.matches(input))
            || StreamEx.of(this.providers)
            .anyMatch(provider -> provider.hasRecipeFor(input));
    }

    @Override
    public R getRecipeFor(I input) {
        return StreamEx.of(this.recipes)
            .filter(r -> r.matches(input))
            .min(RecipeUtil::compareCount)
            .orElseGet(() -> getProvidedRecipe(input));
    }

    protected R getProvidedRecipe(I input) {
        return StreamEx.of(this.providers)
            .mapPartial(provider -> Optional.ofNullable(provider.getRecipeFor(input)))
            .findFirst()
            .orElse(null);
    }
}
