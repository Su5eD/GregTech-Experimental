package dev.su5ed.gtexperimental.compat;

import dev.su5ed.gtexperimental.api.recipe.BaseRecipe;
import dev.su5ed.gtexperimental.api.recipe.RecipeProvider;
import dev.su5ed.gtexperimental.util.TriFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

import static dev.su5ed.gtexperimental.util.GtUtil.location;

public class TranslatingRecipeProvider<FROM, TO extends BaseRecipe<?, ?, IN, ?, ? super TO>, IN> implements RecipeProvider<TO, IN> {
    private final Map<FROM, TO> recipeCache = new HashMap<>();
    private final Map<ResourceLocation, TO> byName = new HashMap<>();
    
    private final String name;
    private final BiPredicate<Level, IN> foreignRecipeChecker;
    private final BiFunction<Level, IN, FROM> foreignRecipeGetter;
    private final BiFunction<IN, FROM, String> idProvider;
    private final TriFunction<ResourceLocation, IN, FROM, TO> translator;

    public TranslatingRecipeProvider(String name, BiPredicate<Level, IN> foreignRecipeChecker, BiFunction<Level, IN, FROM> foreignRecipeGetter, BiFunction<IN, FROM, String> idProvider, TriFunction<ResourceLocation, IN, FROM, TO> translator) {
        this.name = name;
        this.foreignRecipeChecker = foreignRecipeChecker;
        this.foreignRecipeGetter = foreignRecipeGetter;
        this.idProvider = idProvider;
        this.translator = translator;
    }

    @Nullable
    @Override
    public TO getRecipeFor(Level level, IN input) {
        FROM foreignRecipe = this.foreignRecipeGetter.apply(level, input);
        if (foreignRecipe != null) {
            return this.recipeCache.computeIfAbsent(foreignRecipe, recipe -> {
                ResourceLocation id = location("translated", this.name, this.idProvider.apply(input, recipe));
                TO localRecipe = this.translator.apply(id, input, recipe);
                this.byName.put(id, localRecipe);
                return localRecipe;
            });
        }
        return null;
    }

    @Nullable
    @Override
    public TO getById(Level level, ResourceLocation id) {
        return this.byName.get(id);
    }

    @Override
    public boolean hasRecipeFor(Level level, IN input) {
        return this.foreignRecipeChecker.test(level, input);
    }

    @Override
    public void reset() {
        this.recipeCache.clear();
    }
}
