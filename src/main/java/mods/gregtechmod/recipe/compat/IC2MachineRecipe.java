package mods.gregtechmod.recipe.compat;

import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.recipe.Recipe;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Used to convert IC2 machine recipes to GTE format for use in automatic basic machines
 */
public class IC2MachineRecipe extends Recipe<IRecipeIngredient, List<ItemStack>> {

    public IC2MachineRecipe(IRecipeIngredient input, List<ItemStack> output, int duration, double energyCost) {
        super(input, output, duration, energyCost);
    }
}
