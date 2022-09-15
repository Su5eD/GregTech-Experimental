package mods.gregtechmod.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.recipe.fuel.IFuel;
import mods.gregtechmod.api.recipe.fuel.IFuelManager;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.Optional;

import java.util.Arrays;
import java.util.List;

public class CraftTweakerFuels {

    public static void addSimpleFuel(IFuelManager<IFuel<IRecipeIngredient>, ItemStack> manager, IIngredient input, double energy, @Optional IItemStack output) {
        IRecipeIngredient ingredient = RecipeInputConverter.of(input);
        ItemStack outputStack = CraftTweakerMC.getItemStack(output);
        IFuel<IRecipeIngredient> fuel = GregTechAPI.getFuelFactory().makeFuel(ingredient, outputStack, energy);
        CraftTweakerAPI.apply(new AddFuelAction<>(manager, fuel));
    }

    public static void addMultiFuel(IFuelManager<IFuel<IRecipeIngredient>, ItemStack> manager, IIngredient input, double energy, @Optional IItemStack[] output) {
        IRecipeIngredient ingredient = RecipeInputConverter.of(input);
        List<ItemStack> outputStack = Arrays.asList(CraftTweakerMC.getItemStacks(output));
        IFuel<IRecipeIngredient> fuel = GregTechAPI.getFuelFactory().makeFuel(ingredient, outputStack, energy);
        CraftTweakerAPI.apply(new AddFuelAction<>(manager, fuel));
    }
}
