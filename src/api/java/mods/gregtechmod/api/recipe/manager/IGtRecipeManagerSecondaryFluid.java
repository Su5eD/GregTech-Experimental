package mods.gregtechmod.api.recipe.manager;

import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public interface IGtRecipeManagerSecondaryFluid<R extends IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>> extends IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, R> {

    boolean hasRecipeForPrimaryInput(ItemStack input);

    @Override
    R getRecipeFor(List<ItemStack> input);

    R getRecipeFor(ItemStack input, FluidStack fluid);
}
