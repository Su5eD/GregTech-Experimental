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

import java.util.List;

@ModOnly(Reference.MODID)
@ZenClass("mods.gregtechmod.VacuumFreezer")
@ZenRegister
public class VacuumFreezerRecipes {

    @ZenMethod
    public static void addRecipe(IIngredient input, IItemStack output, int duration, double energyCost) {
        IRecipeIngredient ingredient = RecipeInputConverter.of(input);
        ItemStack outputStack = CraftTweakerMC.getItemStack(output);
        IMachineRecipe<IRecipeIngredient, List<ItemStack>> recipe = GregTechAPI.getRecipeFactory().makeVacuumFreezerRecipe(ingredient, outputStack, duration, energyCost);
        CraftTweakerAPI.apply(new AddRecipeAction<>(GtRecipes.vacuumFreezer, recipe));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack input) {
        CraftTweakerAPI.apply(new RemoveRecipeAction<>(GtRecipes.vacuumFreezer, GtRecipes.vacuumFreezer.getRecipeFor(CraftTweakerMC.getItemStack(input))));
    }
}
