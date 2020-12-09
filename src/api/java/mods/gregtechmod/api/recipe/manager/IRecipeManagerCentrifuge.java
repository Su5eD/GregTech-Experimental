package mods.gregtechmod.api.recipe.manager;

import mods.gregtechmod.api.recipe.IGtRecipeManager;
import mods.gregtechmod.api.recipe.IRecipeCentrifuge;
import net.minecraft.item.ItemStack;

public interface IRecipeManagerCentrifuge extends IGtRecipeManager<ItemStack, IRecipeCentrifuge> {

    @Override
    @Deprecated
    default IRecipeCentrifuge getRecipeFor(ItemStack input) {
        return getRecipeFor(input, -1);
    };

    IRecipeCentrifuge getRecipeFor(ItemStack input, int cells);
}
