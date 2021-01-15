package mods.gregtechmod.api.recipe;

import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManager;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerBasic;
import mods.gregtechmod.api.recipe.manager.IRecipeManagerCentrifuge;
import mods.gregtechmod.api.recipe.manager.IRecipeManagerGrinder;
import net.minecraft.item.ItemStack;

import java.util.List;

public class GtRecipes {
    public static IRecipeManagerCentrifuge industrial_centrifuge;
    public static IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, IGtMachineRecipe<List<IRecipeIngredient>, ItemStack>> assembler;
    public static IGtRecipeManager<IRecipeIngredient, ItemStack, IRecipePulverizer> pulverizer;
    public static IRecipeManagerGrinder grinder;
}
