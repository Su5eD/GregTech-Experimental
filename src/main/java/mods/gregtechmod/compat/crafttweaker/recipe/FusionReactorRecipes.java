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
import mods.gregtechmod.api.recipe.IRecipeFusion;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import mods.gregtechmod.api.util.Either;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.compat.crafttweaker.AddRecipeAction;
import mods.gregtechmod.compat.crafttweaker.RecipeInputConverter;
import mods.gregtechmod.compat.crafttweaker.RemoveRecipeAction;
import mods.gregtechmod.compat.crafttweaker.RemoveRecipeByOutputAction;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;
import java.util.List;

@ModOnly(Reference.MODID)
@ZenClass("mods.gregtechmod.FusionReactor")
@ZenRegister
public class FusionReactorRecipes {

    @ZenMethod
    public static void addRecipe(IIngredient[] inputs, ILiquidStack output, int duration, double energyCost, double startEnergy) {
        addRecipe(inputs, Either.right(CraftTweakerMC.getLiquidStack(output)), duration, energyCost, startEnergy);
    }

    @ZenMethod
    public static void addRecipe(IIngredient[] inputs, IItemStack output, int duration, double energyCost, double startEnergy) {
        addRecipe(inputs, Either.left(CraftTweakerMC.getItemStack(output)), duration, energyCost, startEnergy);
    }

    @ZenMethod
    public static void removeRecipe(ILiquidStack[] inputs) {
        List<FluidStack> inputStacks = Arrays.asList(CraftTweakerMC.getLiquidStacks(inputs));
        CraftTweakerAPI.apply(new RemoveRecipeAction<>(GtRecipes.fusion, GtRecipes.fusion.getRecipeFor(inputStacks)));
    }

    @ZenMethod
    public static void removeByOutput(IItemStack output) {
        ItemStack stack = CraftTweakerMC.getItemStack(output);
        CraftTweakerAPI.apply(new RemoveRecipeByOutputAction<>(GtRecipes.fusion, recipeOutput -> recipeOutput.isLeft() && recipeOutput.getLeft().isItemEqual(stack)));
    }

    @ZenMethod
    public static void removeByOutput(ILiquidStack output) {
        FluidStack stack = CraftTweakerMC.getLiquidStack(output);
        CraftTweakerAPI.apply(new RemoveRecipeByOutputAction<>(GtRecipes.fusion, recipeOutput -> recipeOutput.isRight() && recipeOutput.getRight().isFluidEqual(stack)));
    }

    private static void addRecipe(IIngredient[] inputs, Either<ItemStack, FluidStack> output, int duration, double energyCost, double startEnergy) {
        List<IRecipeIngredientFluid> inputIngredients = RecipeInputConverter.fluids(inputs);
        IRecipeFusion recipe = GregTechAPI.getRecipeFactory().makeFusionRecipe(inputIngredients, output, duration, energyCost, startEnergy);
        CraftTweakerAPI.apply(new AddRecipeAction<>(GtRecipes.fusion, recipe));
    }
}
