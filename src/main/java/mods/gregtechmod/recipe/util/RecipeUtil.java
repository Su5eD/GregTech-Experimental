package mods.gregtechmod.recipe.util;

import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.core.GregTechMod;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class RecipeUtil {
    public static boolean validateRecipeIO(String name, List<? extends IRecipeIngredient> input, List<ItemStack> output) {
        return validateRecipeInput(name, input) && validateRecipeOutput(name, output);
    }

    public static boolean validateRecipeIO(String name, IRecipeIngredient input, List<ItemStack> output) {
        return validateRecipeInput(name, Collections.singletonList(input)) && validateRecipeOutput(name, output);
    }

    public static boolean validateRecipeIO(String name, IRecipeIngredient input, ItemStack output) {
        return validateRecipeInput(name, Collections.singletonList(input)) && validateRecipeOutput(name, Collections.singletonList(output));
    }

    public static boolean validateRecipeInput(String name, List<? extends IRecipeIngredient> input) {
        for (IRecipeIngredient ingredient : input) {
            if (ingredient.isEmpty()) {
                GregTechMod.logger.error("Tried to add a(n) " + name + " recipe with an empty ingredient among its inputs. Invalidating...");
                return false;
            }
        }
        return true;
    }

    public static boolean validateRecipeOutput(String name, List<ItemStack> output) {
        if (output.isEmpty()) {
            GregTechMod.logger.error("Tried to add a(n) " + name + " recipe with no output. Invalidating...");
            return false;
        }
        for (ItemStack stack : output) {
            if (stack.isEmpty()) {
                GregTechMod.logger.error("Tried to add a(n) " + name + " recipe with an empty ItemStack among its outputs. Invalidating...");
                return false;
            }
        }
        return true;
    }

    public static <T> List<T> adjustInputCount(String name, List<T> input, List<?> output, int max) {
        if (input.size() > max) {
            GregTechMod.logger.error("Tried to add a " + name + " recipe for " + output + " with too many inputs! Reducing them to "+max);
            return input.subList(0, max);
        }

        return input;
    }

    public static <T> List<T> adjustOutputCount(String name, List<T> output, int max) {
        if (output.size() > max) {
            GregTechMod.logger.error("Tried to add a " + name + " recipe for " + output + " with too many outputs! Reducing them to "+max);
            return output.subList(0, max);
        }

        return output;
    }
}
