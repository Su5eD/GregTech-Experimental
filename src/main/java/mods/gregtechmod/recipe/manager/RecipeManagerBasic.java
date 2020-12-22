package mods.gregtechmod.recipe.manager;

import mods.gregtechmod.api.recipe.IGtMachineRecipe;
import mods.gregtechmod.api.recipe.IRecipeIngredient;
import mods.gregtechmod.util.ItemStackComparator;
import net.minecraft.item.ItemStack;

import java.util.Comparator;

public class RecipeManagerBasic<R extends IGtMachineRecipe<IRecipeIngredient, ?>> extends RecipeManager<IRecipeIngredient, ItemStack, R> {

    public RecipeManagerBasic() {
        super(new RecipeComparator<>());
    }

    public RecipeManagerBasic(Comparator<R> comparator) {
        super(comparator);
    }

    @Override
    public R getRecipeFor(ItemStack input) {
        for (R recipe : this.recipes) {
            if (recipe.getInput().apply(input)) return recipe;
        }
        return null;
    }

    @Override
    public boolean hasRecipeFor(ItemStack input) {
        for (R recipe : this.recipes) {
            if (recipe.getInput().asIngredient().apply(input)) return true;
        }
        return false;
    }

    private static class RecipeComparator<T extends IGtMachineRecipe<IRecipeIngredient, ?>> implements Comparator<T> {

        @Override
        public int compare(T first, T second) {
            int ret = 0;
            for (ItemStack firstInput : first.getInput().asIngredient().getMatchingStacks()) {
                for (ItemStack secondInput : second.getInput().asIngredient().getMatchingStacks()) {
                    ret += ItemStackComparator.INSTANCE.compare(firstInput, secondInput);
                }
            }
            return ret;
        }
    }
}
