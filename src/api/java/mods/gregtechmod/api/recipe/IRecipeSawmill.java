package mods.gregtechmod.api.recipe;

import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public interface IRecipeSawmill extends IMachineRecipe<IRecipeIngredient, List<ItemStack>> {
    FluidStack getRequiredWater();

    boolean isUniversal();
}
