package mods.gregtechmod.recipe.fuel;

import com.google.common.base.MoreObjects;
import mods.gregtechmod.api.recipe.fuel.IFuel;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.core.GregTechMod;
import net.minecraft.item.ItemStack;

import java.util.List;

public abstract class Fuel implements IFuel<IRecipeIngredient> {
    protected final IRecipeIngredient input;
    protected final double energy;
    protected final List<ItemStack> output;
    protected boolean invalid;

    protected Fuel(IRecipeIngredient input, double energy, List<ItemStack> output) {
        this.input = input;
        this.energy = energy;
        this.output = output;
    }

    @Override
    public IRecipeIngredient getInput() {
        return this.input;
    }

    @Override
    public double getEnergy() {
        return this.energy;
    }

    @Override
    public List<ItemStack> getOutput() {
        return this.output;
    }

    @Override
    public boolean isInvalid() {
        return this.invalid;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("input", input)
                .add("energy", energy)
                .add("output", output)
                .add("invalid", invalid)
                .toString();
    }

    protected void validate() {
        if (this.input.isEmpty()) {
            GregTechMod.LOGGER.warn("Tried to add a fuel with empty input: " + this);
            this.invalid = true;
        }
    }
}
