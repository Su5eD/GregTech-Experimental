package dev.su5ed.gregtechmod.api.recipe;

import dev.su5ed.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface IRecipeBlastFurnace extends IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>, IRecipeUniversal<List<IRecipeIngredient>> {
    int getHeat();
}
