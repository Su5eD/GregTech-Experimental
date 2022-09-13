package mods.gregtechmod.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import mods.gregtechmod.api.util.Reference;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;
import java.util.List;

@ModOnly(Reference.MODID)
@ZenClass("mods.gregtechmod.IndustrialGrinder")
@ZenRegister
public class GrinderRecipes {

    @ZenMethod
    public static void addRecipe(IIngredient input, ILiquidStack fluid, IItemStack[] output) {
        IRecipeIngredient inputIngredient = RecipeInputConverter.of(input);
        IRecipeIngredientFluid fluidIngredient = RecipeInputConverter.of(fluid);
        List<ItemStack> outputs = Arrays.asList(CraftTweakerMC.getItemStacks(output));
        IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>> recipe = GregTechAPI.getRecipeFactory().makeGrinderRecipe(inputIngredient, fluidIngredient, outputs);
        CraftTweakerAPI.apply(new AddRecipeAction<>(GtRecipes.industrialGrinder, recipe));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack input, ILiquidStack fluid) {
        ItemStack inputStack = CraftTweakerMC.getItemStack(input);
        FluidStack fluidStack = CraftTweakerMC.getLiquidStack(fluid);
        CraftTweakerAPI.apply(new RemoveRecipeAction<>(GtRecipes.industrialGrinder, GtRecipes.industrialGrinder.getRecipeFor(inputStack, fluidStack)));
    }
}
