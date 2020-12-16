package mods.gregtechmod.api.recipe.manager;

import mods.gregtechmod.api.recipe.IGtRecipeManager;
import mods.gregtechmod.api.recipe.IRecipeCentrifuge;
import mods.gregtechmod.api.recipe.IRecipeIngredient;
import net.minecraft.item.ItemStack;

public interface IRecipeManagerCentrifuge extends IGtRecipeManager<IRecipeIngredient, ItemStack, IRecipeCentrifuge> {

    @Override
    default IRecipeCentrifuge getRecipeFor(ItemStack input) {
        return getRecipeFor(input, -1);
    };

    IRecipeCentrifuge getRecipeFor(ItemStack input, int cells);
}
