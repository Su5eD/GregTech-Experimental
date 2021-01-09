package mods.gregtechmod.api.recipe.manager;

import mods.gregtechmod.api.recipe.IRecipeCentrifuge;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;

public interface IRecipeManagerCentrifuge extends IGtRecipeManagerFluid<IRecipeIngredient, ItemStack, IRecipeCentrifuge> {

    @Override
    default IRecipeCentrifuge getRecipeFor(ItemStack input) {
        return getRecipeFor(input, -1);
    };

    IRecipeCentrifuge getRecipeFor(ItemStack input, int cells);
}
