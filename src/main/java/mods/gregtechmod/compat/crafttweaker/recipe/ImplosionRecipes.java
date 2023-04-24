package mods.gregtechmod.compat.crafttweaker.recipe;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import ic2.api.item.IC2Items;
import ic2.core.util.StackUtil;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.compat.crafttweaker.*;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;
import java.util.List;

@ModOnly(Reference.MODID)
@ZenClass("mods.gregtechmod.ImplosionCompressor")
@ZenRegister
public class ImplosionRecipes {

    @ZenMethod
    public static void addRecipe(IIngredient input, int tnt, IItemStack[] outputs) {
        IRecipeIngredient inputIngredient = RecipeInputConverter.of(input);
        List<ItemStack> outputStacks = Arrays.asList(CraftTweakerMC.getItemStacks(outputs));
        IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>> recipe = GregTechAPI.getRecipeFactory().makeImplosionRecipe(inputIngredient, tnt, outputStacks);
        CraftTweakerAPI.apply(new AddRecipeAction<>(GtRecipes.implosion, recipe));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack input, int tnt) {
        List<ItemStack> inputStacks = Arrays.asList(CraftTweakerMC.getItemStack(input), StackUtil.setSize(IC2Items.getItem("te", "itnt"), tnt));
        CraftTweakerAPI.apply(new RemoveRecipeAction<>(GtRecipes.implosion, GtRecipes.implosion.getRecipeFor(inputStacks)));
    }

    @ZenMethod
    public static void removeByOutput(IItemStack[] outputs) {
        List<ItemStack> stacks = Arrays.asList(CraftTweakerMC.getItemStacks(outputs));
        CraftTweakerAPI.apply(new RemoveRecipeByOutputAction<>(GtRecipes.implosion, CraftTweakerCompat.compareOutputs(stacks)));
    }
}
