package mods.gregtechmod.recipe.manager;

import mods.gregtechmod.api.recipe.IRecipeCentrifuge;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.manager.IRecipeManagerCentrifuge;
import net.minecraft.item.ItemStack;

import java.util.Comparator;

public class RecipeManagerCentrifuge extends RecipeManager<IRecipeIngredient, ItemStack, IRecipeCentrifuge> implements IRecipeManagerCentrifuge {

    public RecipeManagerCentrifuge() {
        super(new CentrifugeRecipeComparator());
    }

    /**
     * @param cells The amount of cells required for this recipe. Pass in -1 to ignore
     */
    @Override
    public IRecipeCentrifuge getRecipeFor(ItemStack input, int cells) {
        for (IRecipeCentrifuge recipe : this.recipes) {
            if (recipe.getInput().apply(input) && (cells < 0 || cells >= recipe.getCells())) return recipe;
        }
        return null;
    }

    @Override
    public boolean hasRecipeFor(ItemStack input) {
        for (IRecipeCentrifuge recipe : this.recipes) {
            if (recipe.getInput().asIngredient().apply(input)) return true;
        }
        return false;
    }

    private static class CentrifugeRecipeComparator implements Comparator<IRecipeCentrifuge> {

        @Override
        public int compare(IRecipeCentrifuge first, IRecipeCentrifuge second) {
            int itemdiff = first.getInput().compareTo(second.getInput());

            if (itemdiff == 0) itemdiff += second.getCells() - first.getCells();

            System.out.println("Comparing "+first.getDuration()+" <-> "+second.getDuration()+" "+itemdiff);

            return itemdiff;
        }
    }
}
