package mods.gregtechmod.recipe.manager;

import mods.gregtechmod.api.recipe.IRecipeSawmill;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerSawmill;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

public class RecipeManagerSawmill extends RecipeManagerBase<IRecipeSawmill> implements IGtRecipeManagerSawmill {

    @Override
    public IRecipeSawmill getRecipeFor(ItemStack input, @Nullable FluidStack fluid) {
        return this.recipes.stream()
                .filter(recipe -> recipe.getInput().apply(input) && (fluid == null || fluid.containsFluid(recipe.getRequiredWater())))
                .min(this::compareCount)
                .orElse(null);
    }
}
