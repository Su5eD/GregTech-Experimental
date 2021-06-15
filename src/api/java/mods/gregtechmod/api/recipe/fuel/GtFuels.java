package mods.gregtechmod.api.recipe.fuel;

import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;

public class GtFuels {
    public static IFuelManagerFluid<IFuel<IRecipeIngredient>> plasma;
    public static IFuelManager<IFuel<IRecipeIngredient>, ItemStack> magic;
    public static IFuelManagerFluid<IFuel<IRecipeIngredient>> diesel;
    public static IFuelManagerFluid<IFuel<IRecipeIngredient>> gas;
    public static IFuelManagerFluid<IFuel<IRecipeIngredient>> hot;
    public static IFuelManagerFluid<IFuel<IRecipeIngredient>> denseLiquid;
}
