package mods.gregtechmod.recipe.fuel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;

public class FuelSimple extends Fuel<ItemStack> {

    private FuelSimple(IRecipeIngredient input, double energy, ItemStack output) {
        super(input, energy, output);
    }

    @JsonCreator
    public static FuelSimple create(@JsonProperty(value = "input", required = true) IRecipeIngredient input,
                                    @JsonProperty(value = "output") ItemStack output,
                                    @JsonProperty(value = "energy", required = true) double energy) {
        FuelSimple fuel = new FuelSimple(input, energy, output == null ? ItemStack.EMPTY : output);
        fuel.validate();
        return fuel;
    }
}
