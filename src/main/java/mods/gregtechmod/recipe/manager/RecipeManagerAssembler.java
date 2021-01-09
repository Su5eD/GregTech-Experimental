package mods.gregtechmod.recipe.manager;

import mods.gregtechmod.api.recipe.IGtMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;

import java.util.Comparator;
import java.util.List;

public class RecipeManagerAssembler extends RecipeManager<List<IRecipeIngredient>, List<ItemStack>, IGtMachineRecipe<List<IRecipeIngredient>, ItemStack>> {

    public RecipeManagerAssembler() {
        super(new AssemblerRecipeComparator());
    }

    @Override
    public IGtMachineRecipe<List<IRecipeIngredient>, ItemStack> getRecipeFor(List<ItemStack> input) {
        for (IGtMachineRecipe<List<IRecipeIngredient>, ItemStack> recipe : this.recipes) {
            List<IRecipeIngredient> recipeInput = recipe.getInput();
            if (recipeInput.get(0).apply(input.get(0)) && recipeInput.get(1).apply(input.get(1))) return recipe;
        }
        return null;
    }

    @Override
    public boolean hasRecipeFor(List<ItemStack> input) {
        for (IGtMachineRecipe<List<IRecipeIngredient>, ItemStack> recipe : this.recipes) {
            List<IRecipeIngredient> recipeInput = recipe.getInput();
            if (recipeInput.get(0).apply(input.get(0), false) && recipeInput.get(1).apply(input.get(1), false)) return true;
        }
        return false;
    }

    private static class AssemblerRecipeComparator implements Comparator<IGtMachineRecipe<List<IRecipeIngredient>, ItemStack>> {

        @Override
        public int compare(IGtMachineRecipe<List<IRecipeIngredient>, ItemStack> first, IGtMachineRecipe<List<IRecipeIngredient>, ItemStack> second) {
            int itemDiff = 0;
            for (IRecipeIngredient firstInput : first.getInput()) {
                for (IRecipeIngredient secondInput : second.getInput()) {
                    itemDiff += firstInput.compareTo(secondInput);
                }
            }
            return itemDiff;
        }
    }
}
