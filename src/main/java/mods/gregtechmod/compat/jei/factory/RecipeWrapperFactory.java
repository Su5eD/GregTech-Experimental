package mods.gregtechmod.compat.jei.factory;

import mezz.jei.api.recipe.IRecipeWrapper;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.fuel.IFuel;
import mods.gregtechmod.api.recipe.fuel.IFuelManager;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerBasic;
import mods.gregtechmod.compat.jei.wrapper.WrapperBasicMachineMulti;
import mods.gregtechmod.compat.jei.wrapper.WrapperBasicMachineSingle;
import net.minecraft.item.ItemStack;
import one.util.streamex.StreamEx;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class RecipeWrapperFactory {

    public static Collection<? extends WrapperBasicMachineSingle<? extends IMachineRecipe<IRecipeIngredient, List<ItemStack>>>> getBasicMachineSingleRecipes(IGtRecipeManagerBasic<IRecipeIngredient, ItemStack, IMachineRecipe<IRecipeIngredient, List<ItemStack>>> manager) {
        return StreamEx.of(manager.getRecipes())
            .remove(recipe -> recipe.getInput().getMatchingInputs().isEmpty())
            .map(WrapperBasicMachineSingle::new)
            .toList();
    }

    public static List<? extends IRecipeWrapper> getBasicMachineMultiRecipes(IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, ? extends IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>> manager) {
        return getMultiRecipes(manager, WrapperBasicMachineMulti::new);
    }

    public static <R extends IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>, W extends IRecipeWrapper> List<? extends W> getMultiRecipes(IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, R> manager, Function<R, W> wrapperFactory) {
        return StreamEx.of(manager.getRecipes())
            .filter(recipe -> StreamEx.of(recipe.getInput())
                .map(IRecipeIngredient::getMatchingInputs)
                .noneMatch(List::isEmpty))
            .map(wrapperFactory)
            .toList();
    }

    public static <F extends IFuel<IRecipeIngredient>, W extends IRecipeWrapper> List<? extends W> getFuelWrappers(IFuelManager<F, ?> fuelManager, Function<F, W> wrapperFactory) {
        return StreamEx.of(fuelManager.getFuels())
            .remove(fuel -> fuel.getInput().getMatchingInputs().isEmpty())
            .map(wrapperFactory)
            .toList();
    }
}
