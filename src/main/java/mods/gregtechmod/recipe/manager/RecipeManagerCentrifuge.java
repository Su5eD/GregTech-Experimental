package mods.gregtechmod.recipe.manager;

import mods.gregtechmod.api.recipe.IRecipeCentrifuge;
import mods.gregtechmod.api.recipe.manager.IRecipeManagerCentrifuge;
import net.minecraft.item.ItemStack;

import java.util.Comparator;

public class RecipeManagerCentrifuge extends RecipeManager<ItemStack, IRecipeCentrifuge, Integer> implements IRecipeManagerCentrifuge {

    public RecipeManagerCentrifuge() {
        super(new CentrifugeRecipeComparator());
    }

    /**
     * @param cells The amount of cells required for this recipe. Pass in -1 to ignore
     */
    @Override
    public IRecipeCentrifuge getRecipeFor(ItemStack input, int cells) {
        for (IRecipeCentrifuge recipe : this.recipes) {
            if (recipe.getInput().isItemEqual(input) && (cells >= 0 == cells >= recipe.getCells())) return recipe;
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
