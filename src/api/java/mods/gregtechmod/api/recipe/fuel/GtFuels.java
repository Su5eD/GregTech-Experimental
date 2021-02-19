package mods.gregtechmod.api.recipe.fuel;

import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

public class GtFuels {
    public static IFuelManager<IFuel<IRecipeIngredientFluid, Fluid>, IRecipeIngredientFluid, Fluid> plasmaFuels;
    public static IFuelManager<IFuel<IRecipeIngredient, ItemStack>, IRecipeIngredient, ItemStack> magicFuels;
}
