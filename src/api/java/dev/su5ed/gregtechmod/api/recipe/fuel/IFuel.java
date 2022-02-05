package dev.su5ed.gregtechmod.api.recipe.fuel;

import dev.su5ed.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface IFuel<I extends IRecipeIngredient> {
    I getInput();

    double getEnergy();

    List<ItemStack> getOutput();

    boolean isInvalid();
}
