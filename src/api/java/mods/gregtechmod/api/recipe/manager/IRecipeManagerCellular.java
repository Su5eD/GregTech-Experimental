package mods.gregtechmod.api.recipe.manager;

import mods.gregtechmod.api.recipe.IRecipeCellular;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;

public interface IRecipeManagerCellular extends IGtRecipeManagerFluid<IRecipeIngredient, ItemStack, IRecipeCellular> {
    IRecipeCellular getRecipeFor(ItemStack input, int cells);
}
