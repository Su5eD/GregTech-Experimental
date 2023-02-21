package dev.su5ed.gtexperimental.api.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface RecipeManager<R extends BaseRecipe<?, IN, ?, ? super R>, IN> extends RecipeProvider<R, IN> {
    List<R> getRecipes(Level level);

    @Nullable
    R getById(Level level, ResourceLocation id);

    void registerProvider(RecipeProvider<R, IN> provider);
}
