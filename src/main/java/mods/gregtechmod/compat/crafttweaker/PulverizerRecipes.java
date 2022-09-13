package mods.gregtechmod.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.recipe.IRecipePulverizer;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.recipe.RecipePulverizer;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;
import java.util.List;

@ModOnly(Reference.MODID)
@ZenClass("mods.gregtechmod.Pulverizer")
@ZenRegister
public class PulverizerRecipes {

    @ZenMethod
    public static void addRecipe(IIngredient input, IItemStack output, @Optional boolean overwrite, @Optional(valueBoolean = true) boolean universal) {
        addRecipe(input, new IItemStack[] { output }, RecipePulverizer.DEFAULT_CHANCE, overwrite, universal);
    }

    @ZenMethod
    public static void addRecipe(IIngredient input, IItemStack[] outputs, int chance, @Optional boolean overwrite, @Optional(valueBoolean = true) boolean universal) {
        IRecipeIngredient inputIngredient = RecipeInputConverter.of(input);
        List<ItemStack> outputStacks = Arrays.asList(CraftTweakerMC.getItemStacks(outputs));
        IRecipePulverizer recipe = GregTechAPI.getRecipeFactory().makePulverizerRecipe(inputIngredient, outputStacks, chance, overwrite, universal);
        CraftTweakerAPI.apply(new AddRecipeAction<>(GtRecipes.pulverizer, recipe));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack input) {
        CraftTweakerAPI.apply(new RemoveRecipeAction<>(GtRecipes.pulverizer, GtRecipes.pulverizer.getRecipeFor(CraftTweakerMC.getItemStack(input))));
    }
}
