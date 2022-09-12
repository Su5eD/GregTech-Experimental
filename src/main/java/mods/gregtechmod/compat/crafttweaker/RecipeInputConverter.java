package mods.gregtechmod.compat.crafttweaker;

import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.oredict.IOreDictEntry;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.recipe.IRecipeIngredientFactory;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;

public final class RecipeInputConverter {

    public static IRecipeIngredient of(IIngredient ingredient) {
        IRecipeIngredientFactory factory = GregTechAPI.getIngredientFactory();
        if (ingredient instanceof IItemStack) {
            return factory.fromStack(CraftTweakerMC.getItemStack((IItemStack) ingredient));
        }
        else if (ingredient instanceof ILiquidStack) {
            return factory.fromFluidStack(CraftTweakerMC.getLiquidStack((ILiquidStack) ingredient));
        }
        else if (ingredient instanceof IOreDictEntry) {
            return factory.fromOre(((IOreDictEntry) ingredient).getName(), ingredient.getAmount());
        }
        return new CraftTweakerRecipeIngredient(ingredient);
    }

    private RecipeInputConverter() {}
}
