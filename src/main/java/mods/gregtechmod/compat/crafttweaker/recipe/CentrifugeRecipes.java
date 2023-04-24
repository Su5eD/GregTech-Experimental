package mods.gregtechmod.compat.crafttweaker.recipe;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.recipe.CellType;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.recipe.IRecipeCellular;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.compat.crafttweaker.*;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;
import java.util.List;

@ModOnly(Reference.MODID)
@ZenClass("mods.gregtechmod.IndustrialCentrifuge")
@ZenRegister
public class CentrifugeRecipes {

    @ZenMethod
    public static void addRecipe(IIngredient input, IItemStack[] outputs, int cells, int duration, @Optional CTCellType cellType) {
        IRecipeIngredient ingredient = RecipeInputConverter.of(input);
        List<ItemStack> outputStacks = Arrays.asList(CraftTweakerMC.getItemStacks(outputs));
        IRecipeCellular recipe = GregTechAPI.getRecipeFactory().makeCentrifugeRecipe(ingredient, outputStacks, cells, duration, cellType != null ? cellType.getCellType() : CellType.CELL);
        CraftTweakerAPI.apply(new AddRecipeAction<>(GtRecipes.industrialCentrifuge, recipe));
    }

    @ZenMethod
    public static void removeRecipe(ILiquidStack input) {
        removeRecipe(input, -1);
    }

    @ZenMethod
    public static void removeRecipe(ILiquidStack input, int cells) {
        CraftTweakerAPI.apply(new RemoveRecipeAction<>(GtRecipes.industrialCentrifuge, GtRecipes.industrialCentrifuge.getRecipeFor(CraftTweakerMC.getLiquidStack(input), cells)));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack input) {
        removeRecipe(input, -1);
    }

    @ZenMethod
    public static void removeRecipe(IItemStack input, int cells) {
        CraftTweakerAPI.apply(new RemoveRecipeAction<>(GtRecipes.industrialCentrifuge, GtRecipes.industrialCentrifuge.getRecipeFor(CraftTweakerMC.getItemStack(input), cells)));
    }

    @ZenMethod
    public static void removeByOutput(IItemStack[] outputs) {
        List<ItemStack> stacks = Arrays.asList(CraftTweakerMC.getItemStacks(outputs));
        CraftTweakerAPI.apply(new RemoveRecipeByOutputAction<>(GtRecipes.industrialCentrifuge, CraftTweakerCompat.compareOutputs(stacks)));
    }
}
