package mods.gregtechmod.recipe.manager;

import mods.gregtechmod.api.recipe.IRecipeCentrifuge;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class RecipeManagerCentrifuge extends RecipeManager<ItemStack, IRecipeCentrifuge, Integer> {

    public RecipeManagerCentrifuge() {
        super(new CentrifugeRecipeComparator());
    }

    /**
     * @deprecated Use {@link RecipeManagerCentrifuge#getRecipeFor(ItemStack input, Map metadata)} instead
     */
    @Override
    @Deprecated
    public IRecipeCentrifuge getRecipeFor(ItemStack input) {
        return getRecipeFor(input, Collections.emptyMap());
    }

    /**
     * @param metadata Contains the amount of required cells for this recipe under the <code>cells</code> key
     */
    @Override
    public IRecipeCentrifuge getRecipeFor(ItemStack input, Map<String, Integer> metadata) {
        for (IRecipeCentrifuge recipe : this.recipes) {
            if (recipe.getInput().isItemEqual(input) && (!metadata.containsKey("cells") || metadata.get("cells") >= recipe.getCells())) return recipe;
        }
        return null;
    }

    private static class CentrifugeRecipeComparator implements Comparator<IRecipeCentrifuge> {

        @Override
        public int compare(IRecipeCentrifuge first, IRecipeCentrifuge second) {
            int nameDiff = first.getInput().getItem().getRegistryName().compareTo(second.getInput().getItem().getRegistryName());
            int inputCountDiff = second.getInput().getCount() - first.getInput().getCount();
            int cellsDiff = second.getCells() - first.getCells();

            int total = nameDiff + inputCountDiff;
            if (inputCountDiff == 0) total += cellsDiff;

            return total;
        }
    }
}
