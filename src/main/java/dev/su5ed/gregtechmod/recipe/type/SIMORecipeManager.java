package dev.su5ed.gregtechmod.recipe.type;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.server.ServerLifecycleHooks;
import one.util.streamex.StreamEx;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SIMORecipeManager<R extends SIMORecipe<O>, O> {
    private final List<? extends R> recipes;

    public SIMORecipeManager(SIMORecipeType<R, O> recipeType) {
        RecipeManager manager = ServerLifecycleHooks.getCurrentServer().getRecipeManager();
        this.recipes = manager.getAllRecipesFor(recipeType);
    }

    @Nullable
    public R getRecipe(ItemStack input) {
        return StreamEx.of(this.recipes)
            .findFirst(r -> r.matches(input))
            .orElse(null);
    }
}
