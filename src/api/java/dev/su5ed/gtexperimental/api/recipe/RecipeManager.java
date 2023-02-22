package dev.su5ed.gtexperimental.api.recipe;

import net.minecraft.world.level.Level;

import java.util.List;

public interface RecipeManager<R extends BaseRecipe<?, IN, ?, ? super R>, IN> extends RecipeProvider<R, IN> {
    List<R> getRecipes(Level level);

    void registerProvider(RecipeProvider<R, IN> provider);
}
