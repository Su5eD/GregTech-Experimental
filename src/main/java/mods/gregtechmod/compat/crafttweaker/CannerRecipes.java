package mods.gregtechmod.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.util.Reference;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;
import java.util.List;

@ModOnly(Reference.MODID)
@ZenClass("mods.gregtechmod.Canner")
@ZenRegister
public class CannerRecipes {

    @ZenMethod
    public static void addRecipe(IIngredient[] inputs, IItemStack[] outputs, int duration, double energyCost) {
        List<IRecipeIngredient> inputIngredients = RecipeInputConverter.of(inputs);
        List<ItemStack> outputStacks = Arrays.asList(CraftTweakerMC.getItemStacks(outputs));
        IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>> recipe = GregTechAPI.getRecipeFactory().makeCannerRecipe(inputIngredients, outputStacks, duration, energyCost);
        CraftTweakerAPI.apply(new AddRecipeAction<>(GtRecipes.canner, recipe));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack[] inputs) {
        List<ItemStack> inputStacks = Arrays.asList(CraftTweakerMC.getItemStacks(inputs));
        CraftTweakerAPI.apply(new RemoveRecipeAction<>(GtRecipes.canner, GtRecipes.canner.getRecipeFor(inputStacks)));
    }
}
