package mods.gregtechmod.api.recipe.manager;

import mods.gregtechmod.api.recipe.IRecipeCellular;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

public interface IGtRecipeManagerCellular extends IGtRecipeManagerFluid<IRecipeIngredient, ItemStack, IRecipeCellular> {
    @Override
    default IRecipeCellular getRecipeFor(@Nullable FluidStack input) {
        return getRecipeFor(input, -1);
    }

    IRecipeCellular getRecipeFor(@Nullable FluidStack input, int cells);

    IRecipeCellular getRecipeFor(ItemStack input, ItemStack cell);
}
