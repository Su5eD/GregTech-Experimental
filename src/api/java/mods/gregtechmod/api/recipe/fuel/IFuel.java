package mods.gregtechmod.api.recipe.fuel;

import net.minecraft.item.ItemStack;

public interface IFuel<T, I> {
    T getInput();

    double getEnergy();

    boolean apply(I fuel);

    ItemStack getOutput();

    boolean isInvalid();
}
