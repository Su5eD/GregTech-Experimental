package dev.su5ed.gtexperimental.api.recipe;

import net.minecraft.world.level.Level;

public interface RecipeProvider<R extends BaseRecipe<?, I, ? super R>, I> {
    /**
     * Checks if a recipe exists which's input exactly matches the target input provided
     */
    R getRecipeFor(Level level, I input);
    
    boolean hasRecipeFor(Level level, I input);
    
    void reset();
}
