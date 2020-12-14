package mods.gregtechmod.recipe.manager;

import mods.gregtechmod.api.recipe.IGtMachineRecipe;
import mods.gregtechmod.util.ItemStackComparator;
import net.minecraft.item.ItemStack;

import java.util.Comparator;
import java.util.List;

public class RecipeManagerAssembler extends RecipeManager<List<ItemStack>, IGtMachineRecipe<List<ItemStack>, ItemStack>> {

    public RecipeManagerAssembler() {
        super(AssemblerRecipeComparator.INSTANCE);
    }

    @Override
    public IGtMachineRecipe<List<ItemStack>, ItemStack> getRecipeFor(List<ItemStack> input) {
        for (IGtMachineRecipe<List<ItemStack>, ItemStack> recipe : this.recipes) {
            List<ItemStack> recipeInput = recipe.getInput();
            if (recipeInput.get(0).isItemEqual(input.get(0)) && recipeInput.get(1).isItemEqual(input.get(1))) return recipe;
        }
        return null;
    }

    private static class AssemblerRecipeComparator implements Comparator<IGtMachineRecipe<List<ItemStack>, ItemStack>> {
        public static final AssemblerRecipeComparator INSTANCE = new AssemblerRecipeComparator();

        @Override
        public int compare(IGtMachineRecipe<List<ItemStack>, ItemStack> first, IGtMachineRecipe<List<ItemStack>, ItemStack> second) {
            List<ItemStack> firstInput = first.getInput();
            List<ItemStack> secondInput = second.getInput();
            return ItemStackComparator.INSTANCE.compare(firstInput.get(0), secondInput.get(0)) + ItemStackComparator.INSTANCE.compare(firstInput.get(1), secondInput.get(1));
        }
    }
}
