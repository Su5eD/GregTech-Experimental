package mods.gregtechmod.recipe.manager;

import mods.gregtechmod.api.recipe.IRecipeBlastFurnace;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;

import java.util.Comparator;
import java.util.List;

public class RecipeManagerBlastFurnace extends RecipeManager<List<IRecipeIngredient>, ItemStack, IRecipeBlastFurnace> {

    public RecipeManagerBlastFurnace() {
        super(new BlastFurnaceRecipeComparator());
    }

    @Override
    public boolean hasRecipeFor(ItemStack input) {
        return this.recipes.stream()
                .anyMatch(recipe -> recipe.getInput()
                        .stream()
                        .anyMatch(ingredient -> ingredient.apply(input)));
    }

    private static class BlastFurnaceRecipeComparator implements Comparator<IRecipeBlastFurnace> {

        @Override
        public int compare(IRecipeBlastFurnace first, IRecipeBlastFurnace second) {
            int diff = 0;
            for (IRecipeIngredient firstInput : first.getInput()) {
                for (IRecipeIngredient secondInput : second.getInput()) {
                    diff += firstInput.compareTo(secondInput);
                }
            }
            if (diff == 0) diff += second.getHeat() - first.getHeat();
            return diff;
        }
    }
}
