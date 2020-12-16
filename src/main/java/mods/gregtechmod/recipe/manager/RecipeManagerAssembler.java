package mods.gregtechmod.recipe.manager;

import mods.gregtechmod.api.recipe.IGtMachineRecipe;
import mods.gregtechmod.api.recipe.IRecipeIngredient;
import mods.gregtechmod.util.ItemStackComparator;
import net.minecraft.item.ItemStack;

import java.util.Comparator;
import java.util.List;

public class RecipeManagerAssembler extends RecipeManager<List<IRecipeIngredient>, List<ItemStack>, IGtMachineRecipe<List<IRecipeIngredient>, ItemStack>> {

    public RecipeManagerAssembler() {
        super(AssemblerRecipeComparator.INSTANCE);
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
            if (recipeInput.get(0).asIngredient().apply(input.get(0)) && recipeInput.get(1).asIngredient().apply(input.get(1))) return true;
        }
        return false;
    }

    private static class AssemblerRecipeComparator implements Comparator<IGtMachineRecipe<List<IRecipeIngredient>, ItemStack>> {
        public static final AssemblerRecipeComparator INSTANCE = new AssemblerRecipeComparator();

        @Override
        public int compare(IGtMachineRecipe<List<IRecipeIngredient>, ItemStack> first, IGtMachineRecipe<List<IRecipeIngredient>, ItemStack> second) {
            int ret = 0;
            for (int i = 0; i < 2; i++) {
                for (ItemStack firstStack : first.getInput().get(i).getMatchingInputs()) {
                    for (ItemStack secondStack : second.getInput().get(i).getMatchingInputs()) {
                        ret += ItemStackComparator.INSTANCE.compare(firstStack, secondStack);
                    }
                }
            }
            return ret;
        }
    }
}
