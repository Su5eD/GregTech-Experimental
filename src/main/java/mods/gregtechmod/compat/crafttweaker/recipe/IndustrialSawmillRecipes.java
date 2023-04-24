package mods.gregtechmod.compat.crafttweaker.recipe;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.recipe.IRecipeUniversal;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.compat.crafttweaker.*;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;
import java.util.List;

@ModOnly(Reference.MODID)
@ZenClass("mods.gregtechmod.IndustrialSawmill")
@ZenRegister
public class IndustrialSawmillRecipes {

    @ZenMethod
    public static void addRecipe(IIngredient input, IItemStack[] outputs, @Optional int water, @Optional boolean universal) {
        IRecipeIngredient ingredient = RecipeInputConverter.of(input);
        List<ItemStack> outputStacks = Arrays.asList(CraftTweakerMC.getItemStacks(outputs));
        IRecipeUniversal<List<IRecipeIngredient>> recipe = GregTechAPI.getRecipeFactory().makeSawmillRecipe(ingredient, outputStacks, water, universal);
        CraftTweakerAPI.apply(new AddRecipeAction<>(GtRecipes.industrialSawmill, recipe));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack input, ILiquidStack fluid) {
        ItemStack inputStack = CraftTweakerMC.getItemStack(input);
        FluidStack fluidStack = CraftTweakerMC.getLiquidStack(fluid);
        CraftTweakerAPI.apply(new RemoveRecipeAction<>(GtRecipes.industrialSawmill, GtRecipes.industrialSawmill.getRecipeFor(inputStack, fluidStack)));
    }

    @ZenMethod
    public static void removeByOutput(IItemStack[] outputs) {
        List<ItemStack> stacks = Arrays.asList(CraftTweakerMC.getItemStacks(outputs));
        CraftTweakerAPI.apply(new RemoveRecipeByOutputAction<>(GtRecipes.industrialSawmill, CraftTweakerCompat.compareOutputs(stacks)));
    }
}
