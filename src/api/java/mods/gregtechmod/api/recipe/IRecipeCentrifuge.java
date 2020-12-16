package mods.gregtechmod.api.recipe;

import net.minecraft.item.ItemStack;

import java.util.Collection;

public interface IRecipeCentrifuge extends IGtMachineRecipe<IRecipeIngredient, Collection<ItemStack>> {
    int getCells();
}
