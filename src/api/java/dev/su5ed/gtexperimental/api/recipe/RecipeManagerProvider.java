package dev.su5ed.gtexperimental.api.recipe;

import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public interface RecipeManagerProvider<R extends BaseRecipe<?, I, ? super R>, I> {
    RecipeManager<R, I> get(Level level);
    
    void registerProvider(Supplier<RecipeProvider<R, I>> provider);
}
