package mods.gregtechmod.recipe.fuel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

public class FuelFluid extends Fuel<IRecipeIngredientFluid, Fluid> {

    public FuelFluid(IRecipeIngredientFluid fuel, double energy, ItemStack output) {
        super(fuel, energy, output);
    }

    @JsonCreator
    public static FuelFluid create(@JsonProperty(value = "input", required = true) IRecipeIngredientFluid input,
                                   @JsonProperty(value = "energy", required = true) double energy,
                                   @JsonProperty(value = "output") ItemStack output) {
        FuelFluid fuel = new FuelFluid(input, energy, output == null ? ItemStack.EMPTY : output);
        if (input.isEmpty()) {
            GregTechAPI.logger.warn("Tried to add a fluid fuel with empty input. Invalidating...");
            fuel.invalid = true;
        }
        return fuel;
    }

    @Override
    public boolean apply(Fluid fuel) {
        return this.input.apply(fuel);
    }
}
