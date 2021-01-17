package mods.gregtechmod.util;

import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class RecipeUtil {
    public static boolean validateRecipeIO(String name, List<IRecipeIngredient> input, List<ItemStack> output) {
        return validateRecipeInput(name, input) && validateRecipeOutput(name, output);
    }

    public static boolean validateRecipeIO(String name, IRecipeIngredient input, List<ItemStack> output) {
        return validateRecipeInput(name, Collections.singletonList(input)) && validateRecipeOutput(name, output);
    }

    public static boolean validateRecipeInput(String name, List<IRecipeIngredient> input) {
        for (IRecipeIngredient ingredient : input) {
            if (ingredient == null) {
                GregTechAPI.logger.error("Tried to add a " + name + " recipe with null input. Invalidating...");
                return false;
            }
        }

        return true;
    }

    public static boolean validateRecipeOutput(String name, List<ItemStack> output) {
        for (ItemStack stack : output) {
            if (stack.isEmpty()) {
                GregTechAPI.logger.error("Tried to add a " + name + " recipe with an empty ItemStack among its outputs. Invalidating...");
                return false;
            }
        }

        return true;
    }
}
