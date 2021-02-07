package mods.gregtechmod.recipe.manager;

import mods.gregtechmod.api.recipe.IGtMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerBasic;
import net.minecraft.item.ItemStack;

import java.util.Comparator;
import java.util.List;

public class RecipeManagerMultiInput<R extends IGtMachineRecipe<List<IRecipeIngredient>, ?>> extends RecipeManager<List<IRecipeIngredient>, List<ItemStack>, R> implements IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, R> {

    public RecipeManagerMultiInput() {
        super(new MultiInputRecipeComparator<>());
    }

    protected RecipeManagerMultiInput(Comparator<R> comparator) {
        super(comparator);
    }

    @Override
    public boolean hasRecipeFor(List<ItemStack> input) {
        return this.recipes.stream()
                .anyMatch(recipe -> recipe.getInput().stream()
                        .allMatch(ingredient -> input.stream()
                                .allMatch(stack -> ingredient.apply(stack, false))));
    }

    @Override
    public R getRecipeFor(List<ItemStack> input) {
        return this.recipes.stream()
                .filter(recipe -> recipe.getInput().stream()
                        .allMatch(ingreident -> input.stream()
                                .allMatch(ingreident::apply)))
                .findFirst().orElse(null);
    }

    protected static class MultiInputRecipeComparator<R extends IGtMachineRecipe<List<IRecipeIngredient>, ?>> implements Comparator<R> {

        @Override
        public int compare(R first, R second) {
            int total = 0;
            for (IRecipeIngredient firstInput : first.getInput()) {
                for (IRecipeIngredient secondInput : second.getInput()) {
                    total += Math.abs(firstInput.compareTo(secondInput));
                }
            }
            return total;
        }
    }
}
