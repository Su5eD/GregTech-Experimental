package mods.gregtechmod.api.recipe.fuel;

import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface IFuel<I extends IRecipeIngredient> {
    I getInput();

    double getEnergy();

    List<ItemStack> getOutput();

    boolean isInvalid();
}
