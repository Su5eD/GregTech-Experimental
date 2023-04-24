package mods.gregtechmod.compat.crafttweaker.recipe;

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
import mods.gregtechmod.compat.crafttweaker.*;
import mods.gregtechmod.recipe.RecipeLathe;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;
import java.util.List;

@ModOnly(Reference.MODID)
@ZenClass("mods.gregtechmod.Lathe")
@ZenRegister
public class LatheRecipes {

    @ZenMethod
    public static void addRecipe(IIngredient input, IItemStack[] outputs, int duration, @Optional(valueDouble = RecipeLathe.DEFAULT_ENERGY_COST) double energyCost) {
        IRecipeIngredient ingredient = RecipeInputConverter.of(input);
        List<ItemStack> outputStacks = Arrays.asList(CraftTweakerMC.getItemStacks(outputs));
        IMachineRecipe<IRecipeIngredient, List<ItemStack>> recipe = GregTechAPI.getRecipeFactory().makeLatheRecipe(ingredient, outputStacks, duration, energyCost);
        CraftTweakerAPI.apply(new AddRecipeAction<>(GtRecipes.lathe, recipe));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack input) {
        CraftTweakerAPI.apply(new RemoveRecipeAction<>(GtRecipes.lathe, GtRecipes.lathe.getRecipeFor(CraftTweakerMC.getItemStack(input))));
    }

    @ZenMethod
    public static void removeByOutput(IItemStack[] outputs) {
        List<ItemStack> stacks = Arrays.asList(CraftTweakerMC.getItemStacks(outputs));
        CraftTweakerAPI.apply(new RemoveRecipeByOutputAction<>(GtRecipes.lathe, CraftTweakerCompat.compareOutputs(stacks)));
    }
}
