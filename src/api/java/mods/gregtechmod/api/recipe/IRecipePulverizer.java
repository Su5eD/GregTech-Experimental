package mods.gregtechmod.api.recipe;

import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface IRecipePulverizer extends IMachineRecipe<IRecipeIngredient, List<ItemStack>>, IRecipeUniversal<IRecipeIngredient> {
    ItemStack getPrimaryOutput();

    ItemStack getSecondaryOutput();

    int getChance();

    boolean shouldOverwrite();
}
