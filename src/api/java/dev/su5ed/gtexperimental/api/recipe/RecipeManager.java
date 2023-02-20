package dev.su5ed.gtexperimental.api.recipe;

import net.minecraft.world.level.Level;

import java.util.List;

public interface RecipeManager<R extends BaseRecipe<?, I, ? super R>, I> extends RecipeProvider<R, I> {
    List<R> getRecipes(Level level);

    void registerProvider(RecipeProvider<R, I> provider);
}
