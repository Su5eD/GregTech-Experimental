package mods.gregtechmod.recipe.manager;

import mods.gregtechmod.api.recipe.IGtMachineRecipe;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.Map;

public class RecipeManagerBasic<R extends IGtMachineRecipe<ItemStack, ?>, M> extends RecipeManager<ItemStack, R, M> {

    @Override
    public R getRecipeFor(ItemStack input) {
        return getRecipeFor(input, Collections.emptyMap());
    }

    @Override
    public R getRecipeFor(ItemStack input, Map<String, M> metadata) {
        for (R recipe : this.recipes) {
            if (recipe.getInput().isItemEqual(input)) return recipe;
        }
        return null;
    }
}
