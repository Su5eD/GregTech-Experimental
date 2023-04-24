package mods.gregtechmod.compat.crafttweaker.recipe;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.recipe.IRecipePrinter;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.compat.crafttweaker.AddRecipeAction;
import mods.gregtechmod.compat.crafttweaker.RecipeInputConverter;
import mods.gregtechmod.compat.crafttweaker.RemoveRecipeAction;
import mods.gregtechmod.compat.crafttweaker.RemoveRecipeByOutputAction;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

@ModOnly(Reference.MODID)
@ZenClass("mods.gregtechmod.Printer")
@ZenRegister
public class PrinterRecipes {

    @ZenMethod
    public static void addRecipe(IIngredient[] inputs, IItemStack output, int duration, @Optional double energyCost) {
        addRecipe(inputs, null, output, duration, energyCost);
    }

    @ZenMethod
    public static void addRecipe(IIngredient[] inputs, @Nullable IIngredient copy, IItemStack output, int duration, @Optional double energyCost) {
        List<IRecipeIngredient> inputIngredients = RecipeInputConverter.of(inputs);
        IRecipeIngredient copyIngredient = copy != null ? RecipeInputConverter.of(copy) : null;
        ItemStack outputStack = CraftTweakerMC.getItemStack(output);
        IRecipePrinter recipe = GregTechAPI.getRecipeFactory().makePrinterRecipe(inputIngredients, copyIngredient, outputStack, duration, energyCost);
        CraftTweakerAPI.apply(new AddRecipeAction<>(GtRecipes.printer, recipe));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack[] inputs) {
        List<ItemStack> inputStacks = Arrays.asList(CraftTweakerMC.getItemStacks(inputs));
        CraftTweakerAPI.apply(new RemoveRecipeAction<>(GtRecipes.printer, GtRecipes.printer.getRecipeFor(inputStacks)));
    }

    @ZenMethod
    public static void removeByOutput(IItemStack output) {
        ItemStack stack = CraftTweakerMC.getItemStack(output);
        CraftTweakerAPI.apply(new RemoveRecipeByOutputAction<>(GtRecipes.printer, stacks -> stacks.stream().anyMatch(stack::isItemEqual)));
    }
}
