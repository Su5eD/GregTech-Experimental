package dev.su5ed.gtexperimental.recipe.type;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.server.ServerLifecycleHooks;
import one.util.streamex.StreamEx;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class BaseRecipeManager<R extends BaseRecipe<?, I, ? super R>, I> implements RecipeManager<R, I> {
    private final Lazy<List<R>> recipes;
    private final Collection<RecipeProvider<R, I>> providers;

    public BaseRecipeManager(RecipeType<R> recipeType) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        this.recipes = Lazy.of(() -> server.getRecipeManager().getAllRecipesFor(recipeType));
        this.providers = new HashSet<>();
    }

    public List<R> getRecipes() {
        return this.recipes.get();
    }

    @Override
    public boolean hasRecipeFor(I input) {
        return StreamEx.of(this.recipes.get())
            .anyMatch(r -> r.matches(input))
            || StreamEx.of(this.providers)
            .anyMatch(provider -> provider.hasRecipeFor(input));
    }

    @Override
    public R getRecipeFor(I input) {
        return StreamEx.of(this.recipes.get())
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

    public void registerProvider(RecipeProvider<R, I> provider) {
        this.providers.add(provider);
    }
}
