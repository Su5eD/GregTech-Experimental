package mods.gregtechmod.compat.crafttweaker.recipe;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.recipe.IRecipeBlastFurnace;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.compat.crafttweaker.*;
import mods.gregtechmod.recipe.RecipeBlastFurnace;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;
import java.util.List;

@ModOnly(Reference.MODID)
@ZenClass("mods.gregtechmod.IndustrialBlastFurnace")
@ZenRegister
public class BlastFurnaceRecipes {

    @ZenMethod
    public static void addRecipe(IIngredient[] inputs, IItemStack[] outputs, int duration, int heat, @Optional boolean universal) {
        addRecipe(inputs, outputs, duration, RecipeBlastFurnace.DEFAULT_ENERGY_COST, heat, universal);
    }

    @ZenMethod
    public static void addRecipe(IIngredient[] inputs, IItemStack[] outputs, int duration, double energyCost, int heat, @Optional boolean universal) {
        List<IRecipeIngredient> inputIngredients = RecipeInputConverter.of(inputs);
        List<ItemStack> outputStacks = Arrays.asList(CraftTweakerMC.getItemStacks(outputs));
        IRecipeBlastFurnace recipe = GregTechAPI.getRecipeFactory().makeBlastFurnaceRecipe(inputIngredients, outputStacks, duration, energyCost, heat, universal);
        CraftTweakerAPI.apply(new AddRecipeAction<>(GtRecipes.industrialBlastFurnace, recipe));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack[] inputs) {
        List<ItemStack> inputStacks = Arrays.asList(CraftTweakerMC.getItemStacks(inputs));
        CraftTweakerAPI.apply(new RemoveRecipeAction<>(GtRecipes.industrialBlastFurnace, GtRecipes.industrialBlastFurnace.getRecipeFor(inputStacks)));
    }

    @ZenMethod
    public static void removeByOutput(IItemStack[] outputs) {
        List<ItemStack> stacks = Arrays.asList(CraftTweakerMC.getItemStacks(outputs));
        CraftTweakerAPI.apply(new RemoveRecipeByOutputAction<>(GtRecipes.industrialBlastFurnace, CraftTweakerCompat.compareOutputs(stacks)));
    }
}
