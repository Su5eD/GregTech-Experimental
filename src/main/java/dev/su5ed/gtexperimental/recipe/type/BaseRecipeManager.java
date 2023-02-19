package dev.su5ed.gtexperimental.recipe.type;

import dev.su5ed.gtexperimental.api.recipe.BaseRecipe;
import dev.su5ed.gtexperimental.api.recipe.RecipeManager;
import dev.su5ed.gtexperimental.api.recipe.RecipeProvider;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import one.util.streamex.StreamEx;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class BaseRecipeManager<R extends BaseRecipe<?, I, ? super R>, I> implements RecipeManager<R, I> {
    private final Supplier<? extends RecipeType<R>> recipeType;
    private final Collection<RecipeProvider<R, I>> providers = new ArrayList<>();

    private List<R> recipes;

    public BaseRecipeManager(Supplier<? extends RecipeType<R>> recipeType) {
        this.recipeType = recipeType;
    }

    @Override
    public List<R> getRecipes(Level level) {
        if (this.recipes == null) {
            this.recipes = level.getRecipeManager().getAllRecipesFor(this.recipeType.get());
        }
        return this.recipes;
    }

    @Override
    public boolean hasRecipeFor(Level level, I input) {
        return StreamEx.of(getRecipes(level))
            .anyMatch(r -> r.matches(input))
            || StreamEx.of(this.providers)
            .anyMatch(provider -> provider.hasRecipeFor(level, input));
    }

    @Override
    public R getRecipeFor(Level level, I input) {
        return StreamEx.of(getRecipes(level))
            .filter(r -> r.matches(input))
            .min(RecipeUtil::compareCount)
            .orElseGet(() -> getProvidedRecipe(level, input));
    }

    @Override
    public void registerProvider(RecipeProvider<R, I> provider) {
        this.providers.add(provider);
    }

    @Override
    public void reset() {
        this.recipes = null;
    }

    protected R getProvidedRecipe(Level level, I input) {
        return StreamEx.of(this.providers)
            .mapPartial(provider -> Optional.ofNullable(provider.getRecipeFor(level, input)))
            .findFirst()
            .orElse(null);
    }
}
