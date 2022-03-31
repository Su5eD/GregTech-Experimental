package mods.gregtechmod.recipe.util;

import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class RecipeUtil {

    public static boolean validateRecipeIO(String name, List<? extends IRecipeIngredient> input, ItemStack output) {
        return validateRecipeIO(name, input, Collections.singletonList(output));
    }

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
                throw new RuntimeException("Tried to add a(n) " + name + " recipe with an empty ingredient among its inputs.");
            }
        }
        return true;
    }

    public static boolean validateRecipeOutput(String name, List<ItemStack> output) {
        if (output.isEmpty()) {
            throw new RuntimeException("Tried to add a(n) " + name + " recipe with no output.");
        }
        for (ItemStack stack : output) {
            if (stack.isEmpty()) {
                throw new RuntimeException("Tried to add a(n) " + name + " recipe with an empty ItemStack among its outputs.");
            }
        }
        return true;
    }

    public static <T> List<T> adjustInputCount(String name, List<T> input, Object output, int max) {
        return adjustInputCount(name, input, Collections.singletonList(output), max);
    }

    public static <T> List<T> adjustInputCount(String name, List<T> input, List<?> output, int max) {
        if (input.size() > max) {
            throw new RuntimeException("Tried to add a " + name + " recipe for " + output + " with too many inputs!");
        }

        return input;
    }

    public static <T> List<T> adjustOutputCount(String name, List<T> output, int max) {
        if (output.size() > max) {
            throw new RuntimeException("Tried to add a " + name + " recipe for " + output + " with too many outputs!");
        }

        return output;
    }
}
