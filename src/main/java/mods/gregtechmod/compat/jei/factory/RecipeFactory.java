package mods.gregtechmod.compat.jei.factory;

import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerBasic;
import mods.gregtechmod.compat.jei.wrapper.WrapperBasicMachine;
import mods.gregtechmod.compat.jei.wrapper.WrapperBasicMachineMulti;
import mods.gregtechmod.compat.jei.wrapper.WrapperBasicMachineSingle;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class RecipeFactory {
    public static List<? extends WrapperBasicMachine<IMachineRecipe<IRecipeIngredient, List<ItemStack>>>> getBasicMachineSingleRecipes(IGtRecipeManagerBasic<IRecipeIngredient, ItemStack, IMachineRecipe<IRecipeIngredient, List<ItemStack>>> manager) {
        return manager.getRecipes()
                .stream()
                .filter(recipe -> !recipe.getInput().getMatchingInputs().isEmpty())
                .map(WrapperBasicMachineSingle::new)
                .collect(Collectors.toList());
    }

    public static List<? extends WrapperBasicMachine<? extends IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>>> getBasicMachineMultiRecipes(IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, ? extends IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>> manager) {
        return manager.getRecipes()
                .stream()
                .filter(recipe -> recipe.getInput()
                        .stream()
                        .noneMatch(IRecipeIngredient::isEmpty))
                .map(WrapperBasicMachineMulti::new)
                .collect(Collectors.toList());
    }
}
