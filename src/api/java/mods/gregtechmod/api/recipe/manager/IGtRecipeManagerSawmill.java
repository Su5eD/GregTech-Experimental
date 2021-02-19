package mods.gregtechmod.api.recipe.manager;

import mods.gregtechmod.api.recipe.IRecipeSawmill;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

public interface IGtRecipeManagerSawmill extends IGtRecipeManagerBasic<IRecipeIngredient, ItemStack, IRecipeSawmill> {
    @Override
    default IRecipeSawmill getRecipeFor(ItemStack input) {
        return getRecipeFor(input, null);
    }

    IRecipeSawmill getRecipeFor(ItemStack input, @Nullable FluidStack fluid);
}
