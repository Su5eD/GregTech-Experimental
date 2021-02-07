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

    public static boolean validateRecipeIO(String name, IRecipeIngredient input, ItemStack output) {
        return validateRecipeInput(name, Collections.singletonList(input)) && validateRecipeOutput(name, Collections.singletonList(output));
    }

    public static boolean validateRecipeInput(String name, List<IRecipeIngredient> input) {
        if (input.contains(null)) {
            GregTechAPI.logger.error("Tried to add a(n) " + name + " recipe with a null among its inputs. Invalidating...");
            return false;
        }
        return true;
    }

    public static boolean validateRecipeOutput(String name, List<ItemStack> output) {
        for (ItemStack stack : output) {
            if (stack.isEmpty()) {
                GregTechAPI.logger.error("Tried to add a(n) " + name + " recipe with an empty ItemStack among its outputs. Invalidating...");
                return false;
            }
        }
        return true;
    }

    public static void adjustRecipeIOCount(String name, List<?> input, List<?> output, int maxIn, int maxOut) {
        adjustInputCount(name, input, output, maxIn);
        adjustOutputCount(name, output, maxOut);
    }

    public static void adjustInputCount(String name, List<?> input, List<?> output, int max) {
        if (input.size() > max) {
            GregTechAPI.logger.error("Tried to add a " + name + " recipe for " + output + " with too many inputs! Reducing them to "+max);
            input = input.subList(0, max);
        }
    }

    public static void adjustOutputCount(String name, List<?> output, int max) {
        if (output.size() > max) {
            GregTechAPI.logger.error("Tried to add a " + name + " recipe for " + output + " with too many outputs! Reducing them to "+max);
            output = output.subList(0, max);
        }
    }
}
