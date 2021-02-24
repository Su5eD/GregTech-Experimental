package mods.gregtechmod.recipe.fuel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;

public class FuelSolid extends Fuel<IRecipeIngredient, ItemStack> {

    public FuelSolid(IRecipeIngredient input, double energy, ItemStack output) {
        super(input, energy, output == null ? ItemStack.EMPTY : output);
    }

    @JsonCreator
    public static FuelSolid create(@JsonProperty(value = "input", required = true) IRecipeIngredient input,
                                   @JsonProperty(value = "energy", required = true) double energy,
                                   @JsonProperty(value = "output") ItemStack output) {
        FuelSolid fuel = new FuelSolid(input, energy, output == null ? ItemStack.EMPTY : output);
        if (input.isEmpty()) {
            GregTechAPI.logger.warn("Tried to add a solid fuel with empty input. Invalidating...");
            fuel.invalid = true;
        }
        return fuel;
    }

    @Override
    public boolean apply(ItemStack fuel) {
        return this.input.apply(fuel);
    }
}
