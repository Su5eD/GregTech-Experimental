package dev.su5ed.gtexperimental.recipe.type;

import dev.su5ed.gtexperimental.api.recipe.BaseRecipe;
import dev.su5ed.gtexperimental.api.recipe.RecipeManager;
import dev.su5ed.gtexperimental.api.recipe.RecipeManagerProvider;
import dev.su5ed.gtexperimental.api.recipe.RecipeProvider;
import net.minecraft.Util;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import one.util.streamex.StreamEx;

import java.util.Collection;
import java.util.HashSet;
import java.util.function.Function;
import java.util.function.Supplier;

public class BaseRecipeManagerProvider<R extends BaseRecipe<?, I, ? super R>, I> implements RecipeManagerProvider<R, I> {
    private final Function<Level, RecipeManager<R, I>> recipeManagers = Util.memoize(this::createRecipeManager);
    private final Supplier<? extends RecipeType<R>> recipeType;
    private final Collection<Supplier<RecipeProvider<R, I>>> providers = new HashSet<>();
    private final RecipeManagerFactory<R, I> factory;

    public BaseRecipeManagerProvider(Supplier<? extends RecipeType<R>> recipeType, RecipeManagerFactory<R, I> factory) {
        this.recipeType = recipeType;
        this.factory = factory;
    }

    public RecipeManager<R, I> get(Level level) {
        return this.recipeManagers.apply(level);
    }

    public void registerProvider(Supplier<RecipeProvider<R, I>> provider) {
        this.providers.add(provider);
    }

    private RecipeManager<R, I> createRecipeManager(Level level) {
        Collection<RecipeProvider<R, I>> providers = StreamEx.of(this.providers)
            .map(Supplier::get)
            .toImmutableSet();
        return this.factory.create(this.recipeType.get(), level, providers);
    }

    public interface RecipeManagerFactory<R extends BaseRecipe<?, I, ? super R>, I> {
        RecipeManager<R, I> create(RecipeType<R> recipeType, Level level, Collection<RecipeProvider<R, I>> providers);
    }
}
