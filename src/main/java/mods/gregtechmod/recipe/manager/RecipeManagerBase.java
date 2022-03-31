package mods.gregtechmod.recipe.manager;

import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;
import one.util.streamex.StreamEx;

public abstract class RecipeManagerBase<R extends IMachineRecipe<IRecipeIngredient, ?>> extends RecipeManager<IRecipeIngredient, ItemStack, R> {
    @Override
    public boolean hasRecipeFor(ItemStack input) {
        return this.recipes.stream()
            .anyMatch(recipe -> recipe.getInput().apply(input, false));
    }

    @Override
    protected R getRecipeForExact(R recipe) {
        IRecipeIngredient input = recipe.getInput();
        return StreamEx.of(this.recipes)
            .findFirst(r -> r.getInput().apply(input) && compareCount(r, recipe) == 0)
            .orElse(null);
    }

    public int compareCount(R first, R second) {
        return second.getInput().getCount() - first.getInput().getCount();
    }
}
