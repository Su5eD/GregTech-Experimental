package dev.su5ed.gtexperimental.recipe.type;

import dev.su5ed.gtexperimental.api.recipe.BaseRecipe;
import dev.su5ed.gtexperimental.api.recipe.BaseRecipeType;
import dev.su5ed.gtexperimental.api.recipe.RecipeManager;
import dev.su5ed.gtexperimental.api.recipe.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import one.util.streamex.StreamEx;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class BaseRecipeManager<R extends BaseRecipe<?, ?, IN, OUT, ? super R>, IN, OUT> implements RecipeManager<R, IN, OUT> {
    private final Supplier<? extends BaseRecipeType<R, ?, ?, OUT>> recipeType;
    private final Collection<RecipeProvider<R, IN>> providers = new ArrayList<>();

    private List<R> recipes;

    public BaseRecipeManager(Supplier<? extends BaseRecipeType<R, ?, ?, OUT>> recipeType) {
        this.recipeType = recipeType;
    }

    public BaseRecipeType<R, ?, ?, OUT> getRecipeType() {
        return this.recipeType.get();
    }

    @Override
    public List<R> getRecipes(Level level) {
        if (this.recipes == null) {
            this.recipes = level.getRecipeManager().getAllRecipesFor(this.recipeType.get());
        }
        return this.recipes;
    }

    @Override
    public boolean hasRecipeFor(Level level, IN input) {
        return StreamEx.of(getRecipes(level))
            .anyMatch(r -> r.matchesPartial(input))
            || StreamEx.of(this.providers)
            .anyMatch(provider -> provider.hasRecipeFor(level, input));
    }

    @Nullable
    @Override
    public R getRecipeFor(Level level, IN input) {
        return StreamEx.of(getRecipes(level))
            .filter(r -> r.matches(input))
            .min(R::compareInputCount)
            .orElseGet(() -> StreamEx.of(this.providers)
                .mapPartial(provider -> Optional.ofNullable(provider.getRecipeFor(level, input)))
                .findFirst()
                .orElse(null));
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public R getById(Level level, ResourceLocation id) {
        return ((Optional<R>) level.getRecipeManager().byKey(id))
            .filter(recipe -> recipe.getType() == this.recipeType.get())
            .orElseGet(() -> StreamEx.of(this.providers)
                .mapPartial(provider -> Optional.ofNullable(provider.getById(level, id)))
                .findFirst()
                .orElse(null));
    }

    @Override
    public void registerProvider(RecipeProvider<R, IN> provider) {
        this.providers.add(provider);
    }

    @Override
    public void reset() {
        this.recipes = null;
    }
}
