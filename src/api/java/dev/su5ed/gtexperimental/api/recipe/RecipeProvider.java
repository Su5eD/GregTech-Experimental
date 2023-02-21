package dev.su5ed.gtexperimental.api.recipe;

import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public interface RecipeProvider<R extends BaseRecipe<?, IN, ?, ? super R>, IN> {
    /**
     * Checks if a recipe exists which's input exactly matches the target input provided
     */
    @Nullable
    R getRecipeFor(Level level, IN input);
    
    boolean hasRecipeFor(Level level, IN input);
    
    void reset();
}
