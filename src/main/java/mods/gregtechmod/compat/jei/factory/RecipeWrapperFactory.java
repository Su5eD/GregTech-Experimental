package mods.gregtechmod.compat.jei.factory;

import mezz.jei.api.recipe.IRecipeWrapper;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerBasic;
import mods.gregtechmod.compat.jei.wrapper.WrapperBasicMachineMulti;
import mods.gregtechmod.compat.jei.wrapper.WrapperBasicMachineSingle;
import net.minecraft.item.ItemStack;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RecipeWrapperFactory {
    
    public static Collection<? extends WrapperBasicMachineSingle<? extends IMachineRecipe<IRecipeIngredient, List<ItemStack>>>> getBasicMachineSingleRecipes(IGtRecipeManagerBasic<IRecipeIngredient, ItemStack, IMachineRecipe<IRecipeIngredient, List<ItemStack>>> manager) {
        return manager.getRecipes()
                .stream()
                .filter(recipe -> !recipe.getInput().getMatchingInputs().isEmpty())
                .map(WrapperBasicMachineSingle::new)
                .collect(Collectors.toList());
    }
    
    public static List<? extends IRecipeWrapper> getBasicMachineMultiRecipes(IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, ? extends IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>> manager) {
        return getMultiRecipes(manager, WrapperBasicMachineMulti::new);
    }

    public static <R extends IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>, W extends IRecipeWrapper> List<? extends W> getMultiRecipes(IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, R> manager, Function<R, W> wrapperFactory) {
        return manager.getRecipes()
                .stream()
                .filter(recipe -> recipe.getInput()
                        .stream()
                        .map(IRecipeIngredient::getMatchingInputs)
                        .noneMatch(List::isEmpty))
                .map(wrapperFactory)
                .collect(Collectors.toList());
    }
}
