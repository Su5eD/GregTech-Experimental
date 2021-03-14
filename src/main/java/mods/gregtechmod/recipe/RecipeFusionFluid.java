package mods.gregtechmod.recipe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.recipe.util.RecipeUtil;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class RecipeFusionFluid extends RecipeFusion<IRecipeIngredientFluid, FluidStack> {

    private RecipeFusionFluid(List<IRecipeIngredientFluid> input, FluidStack output, int duration, double energyCost, double startEnergy) {
        super(input, output, duration, energyCost, startEnergy);
    }

    @JsonCreator
    public static RecipeFusionFluid create(@JsonProperty(value = "input", required = true) List<IRecipeIngredientFluid> input,
                                           @JsonProperty(value = "output", required = true) FluidStack output,
                                           @JsonProperty(value = "duration", required = true) int duration,
                                           @JsonProperty(value = "energyCost", required = true) double energyCost,
                                           @JsonProperty(value = "startEnergy", required = true) double startEnergy) {
        RecipeFusionFluid recipe = new RecipeFusionFluid(input, output, duration, energyCost, startEnergy);

        RecipeUtil.validateRecipeInput("fusion", input);
        if (output == null) {
            GregTechMod.logger.warn("Tried to add a fusion recipe with null output! Invalidating...");
            recipe.invalid = true;
        }

        return recipe;
    }
}
