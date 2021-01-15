package mods.gregtechmod.recipe.manager;

import mods.gregtechmod.api.recipe.IGtMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerBasic;
import net.minecraft.item.ItemStack;

import java.util.Comparator;
import java.util.List;

public class RecipeManagerAssembler extends RecipeManager<List<IRecipeIngredient>, List<ItemStack>, IGtMachineRecipe<List<IRecipeIngredient>, ItemStack>> implements IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, IGtMachineRecipe<List<IRecipeIngredient>, ItemStack>> {

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
        ItemStack firstInput = input.get(0);
        ItemStack secondaryInput = input.get(1);
        return this.recipes.stream()
                .anyMatch(recipe -> {
                    List<IRecipeIngredient> recipeInput = recipe.getInput();
                    IRecipeIngredient firstIngredient = recipeInput.get(0);
                    IRecipeIngredient secondIngredient = recipeInput.get(1);
                    return firstIngredient.apply(firstInput, false) || firstIngredient.apply(secondaryInput, false) || secondIngredient.apply(firstInput, false) || secondIngredient.apply(secondaryInput, false);
                });
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
