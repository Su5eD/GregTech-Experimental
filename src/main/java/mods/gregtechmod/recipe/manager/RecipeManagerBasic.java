package mods.gregtechmod.recipe.manager;

import mods.gregtechmod.api.recipe.IGtMachineRecipe;
import net.minecraft.item.ItemStack;

import java.util.Comparator;

public class RecipeManagerBasic<R extends IGtMachineRecipe<ItemStack, ?>, M> extends RecipeManager<ItemStack, R, M> {

    public RecipeManagerBasic() {
        super(new RecipeComparator<R>());
    }

    public RecipeManagerBasic(Comparator<R> comparator) {
        super(comparator);
    }

    @Override
    public R getRecipeFor(ItemStack input) {
        for (R recipe : this.recipes) {
            if (recipe.getInput().isItemEqual(input)) return recipe;
        }
        return null;
    }

    private static class RecipeComparator<T extends IGtMachineRecipe<ItemStack, ?>> implements Comparator<T> {

        @Override
        public int compare(T first, T second) {
            int nameDiff = first.getInput().getItem().getRegistryName().compareTo(second.getInput().getItem().getRegistryName());
            int inputDiff = second.getInput().getCount() - first.getInput().getCount();

            return nameDiff + inputDiff;
        }
    }
}
