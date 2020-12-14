package mods.gregtechmod.recipe.manager;

import mods.gregtechmod.api.recipe.IGtMachineRecipe;
import mods.gregtechmod.util.ItemStackComparator;
import net.minecraft.item.ItemStack;

import java.util.Comparator;

public class RecipeManagerBasic<R extends IGtMachineRecipe<ItemStack, ?>> extends RecipeManager<ItemStack, R> {

    public RecipeManagerBasic() {
        super(new RecipeComparator<>());
    }

    public RecipeManagerBasic(Comparator<R> comparator) {
        super(comparator);
    }

    @Override
    public R getRecipeFor(ItemStack input) {
        for (R recipe : this.recipes) {
            if (recipe.getInput().isItemEqual(input) && recipe.getInput().getCount() <= input.getCount()) return recipe;
        }
        return null;
    }

    private static class RecipeComparator<T extends IGtMachineRecipe<ItemStack, ?>> implements Comparator<T> {

        @Override
        public int compare(T first, T second) {
            return ItemStackComparator.INSTANCE.compare(first.getInput(), second.getInput());
        }
    }
}
