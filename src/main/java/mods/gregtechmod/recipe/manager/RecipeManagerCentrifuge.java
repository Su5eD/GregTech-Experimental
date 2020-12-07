package mods.gregtechmod.recipe.manager;

import mods.gregtechmod.api.recipe.IRecipeCentrifuge;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.Map;

public class RecipeManagerCentrifuge extends RecipeManager<ItemStack, IRecipeCentrifuge, Integer> {

    @Override
    public IRecipeCentrifuge getRecipeFor(ItemStack input) {
        return getRecipeFor(input, Collections.emptyMap());
    }

    @Override
    public IRecipeCentrifuge getRecipeFor(ItemStack input, Map<String, Integer> metadata) {
        for (IRecipeCentrifuge recipe : this.recipes) {
            if (recipe.getInput().isItemEqual(input) && (!metadata.containsKey("cells") || metadata.get("cells") >= recipe.getCells())) return recipe;
        }
        return null;
    }
}
