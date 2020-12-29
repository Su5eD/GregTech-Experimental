package mods.gregtechmod.api.recipe;

import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.manager.IRecipeManagerCentrifuge;
import net.minecraft.item.ItemStack;

import java.util.List;

public class GtRecipes {
    public static IRecipeManagerCentrifuge industrial_centrifuge;
    public static IGtRecipeManager<List<IRecipeIngredient>, List<ItemStack>, IGtMachineRecipe<List<IRecipeIngredient>, ItemStack>> assembler;
    public static IGtRecipeManager<IRecipeIngredient, ItemStack, IRecipePulverizer> pulverizer;
}
