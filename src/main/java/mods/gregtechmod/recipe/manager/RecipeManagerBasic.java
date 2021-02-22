package mods.gregtechmod.recipe.manager;

import mods.gregtechmod.api.recipe.IGtMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerBasic;
import net.minecraft.item.ItemStack;

public class RecipeManagerBasic<R extends IGtMachineRecipe<IRecipeIngredient, ?>> extends RecipeManagerBase<R> implements IGtRecipeManagerBasic<IRecipeIngredient, ItemStack, R> {

    @Override
    public R getRecipeFor(ItemStack input) {
        return this.recipes.stream()
                .filter(recipe -> recipe.getInput().apply(input))
                .min(this::compareCount)
                .orElse(null);
    }
}
