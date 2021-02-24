package mods.gregtechmod.recipe.fuel;

import mods.gregtechmod.api.recipe.fuel.IFuel;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;

public abstract class Fuel<T extends IRecipeIngredient, I> implements IFuel<T, I> {
    protected final T input;
    protected final double energy;
    protected final ItemStack output;
    protected boolean invalid;

    public Fuel(T input, double energy, ItemStack output) {
        this.input = input;
        this.energy = energy;
        this.output = output;
    }

    @Override
    public T getInput() {
        return this.input;
    }

    @Override
    public double getEnergy() {
        return this.energy;
    }

    @Override
    public ItemStack getOutput() {
        return this.output;
    }

    @Override
    public boolean isInvalid() {
        return this.invalid;
    }
}
