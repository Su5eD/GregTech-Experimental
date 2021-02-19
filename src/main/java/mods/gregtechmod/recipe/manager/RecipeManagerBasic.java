package mods.gregtechmod.recipe.manager;

import mods.gregtechmod.api.recipe.IGtMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerBasic;
import net.minecraft.item.ItemStack;

import java.util.Comparator;

public class RecipeManagerBasic<R extends IGtMachineRecipe<IRecipeIngredient, ?>> extends RecipeManagerBase<R> implements IGtRecipeManagerBasic<IRecipeIngredient, ItemStack, R> {

    public RecipeManagerBasic() {
        super(new RecipeComparator<>());
    }

    public RecipeManagerBasic(Comparator<R> comparator) {
        super(comparator);
    }

    @Override
    public R getRecipeFor(ItemStack input) {
        return this.recipes.stream()
                .filter(recipe -> recipe.getInput().apply(input))
                .findFirst()
                .orElse(null);
    }

    private static class RecipeComparator<T extends IGtMachineRecipe<IRecipeIngredient, ?>> implements Comparator<T> {
        @Override
        public int compare(T first, T second) {
            return first.getInput().compareTo(second.getInput());
        }
    }
}
