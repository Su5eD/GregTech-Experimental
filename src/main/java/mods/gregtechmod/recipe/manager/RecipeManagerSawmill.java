package mods.gregtechmod.recipe.manager;

import mods.gregtechmod.api.recipe.IRecipeSawmill;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerSawmill;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.Comparator;

public class RecipeManagerSawmill extends RecipeManagerBasic<IRecipeSawmill> implements IGtRecipeManagerSawmill {

    public RecipeManagerSawmill() {
        super(RecipeSawmillComparator.INSTANCE);
    }

    @Override
    public IRecipeSawmill getRecipeFor(ItemStack input, @Nullable FluidStack fluid) {
        return this.recipes.stream()
                .filter(recipe -> recipe.getInput().apply(input) && (fluid == null || fluid.containsFluid(recipe.getRequiredWater())))
                .findFirst()
                .orElse(null);
    }

    private static class RecipeSawmillComparator implements Comparator<IRecipeSawmill> {
        public static final RecipeSawmillComparator INSTANCE = new RecipeSawmillComparator();

        @Override
        public int compare(IRecipeSawmill first, IRecipeSawmill second) {
            int diff = first.getInput().compareTo(second.getInput());
            if (diff == 0) diff += second.getRequiredWater().amount - first.getRequiredWater().amount;

            return diff;
        }
    }
}
