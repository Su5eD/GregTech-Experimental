package mods.gregtechmod.util;

import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;

import java.util.List;

public class RecipeUtil {
    public static boolean validateRecipeIO(String name, IRecipeIngredient input, List<ItemStack> output) {
        if (input == null) {
            GregTechAPI.logger.error("Tried to add a " + name + " recipe with null input. Invalidating...");
            return false;
        }

        for (ItemStack stack : output) {
            if (stack.isEmpty()) {
                GregTechAPI.logger.error("Tried to add a " + name + " recipe with an empty ItemStack among its outputs. Invalidating...");
                return true;
            }
        }

        return true;
    }
}
