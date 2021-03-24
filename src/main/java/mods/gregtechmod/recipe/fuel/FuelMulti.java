package mods.gregtechmod.recipe.fuel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class FuelMulti extends Fuel<List<ItemStack>> {

    private FuelMulti(IRecipeIngredient input, double energy, List<ItemStack> output) {
        super(input, energy, output);
    }

    @JsonCreator
    public static FuelMulti create(@JsonProperty(value = "input", required = true) IRecipeIngredient input,
                                   @JsonProperty(value = "output") List<ItemStack> output,
                                   @JsonProperty(value = "energy", required = true) double energy) {
        FuelMulti fuel = new FuelMulti(input, energy, output == null ? Collections.emptyList() : output);
        fuel.validate();
        return fuel;
    }
}
