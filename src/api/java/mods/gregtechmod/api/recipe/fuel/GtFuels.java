package mods.gregtechmod.api.recipe.fuel;

import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;

import java.util.List;

public class GtFuels {
    public static IFuelManagerFluid<IFuel<IRecipeIngredient, ItemStack>> plasma;
    public static IFuelManager<IFuel<IRecipeIngredient, ItemStack>, ItemStack> magic;
    public static IFuelManagerFluid<IFuel<IRecipeIngredient, ItemStack>> diesel;
    public static IFuelManagerFluid<IFuel<IRecipeIngredient, List<ItemStack>>> hot;
    public static IFuelManagerFluid<IFuel<IRecipeIngredient, ItemStack>> denseLiquid;
}
