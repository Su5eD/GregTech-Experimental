package mods.gregtechmod.api.recipe.fuel;

import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;

import java.util.List;

public interface IFuelFactory {
    IFuel<IRecipeIngredient> makeFuel(IRecipeIngredient input, ItemStack output, double energy);
    
    IFuel<IRecipeIngredient> makeFuel(IRecipeIngredient input, List<ItemStack> output, double energy);
}
