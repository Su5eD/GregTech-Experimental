package mods.gregtechmod.recipe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.recipe.util.RecipeUtil;
import net.minecraft.item.ItemStack;

import java.util.List;

public class RecipeFusionSolid extends RecipeFusion<IRecipeIngredient, ItemStack> {

    private RecipeFusionSolid(List<IRecipeIngredient> input, ItemStack output, int duration, double energyCost, double startEnergy) {
        super(input, output, duration, energyCost, startEnergy);
    }

    @JsonCreator
    public static RecipeFusionSolid create(@JsonProperty(value = "input", required = true) List<IRecipeIngredient> input,
                                           @JsonProperty(value = "output", required = true) ItemStack output,
                                           @JsonProperty(value = "duration", required = true) int duration,
                                           @JsonProperty(value = "energyCost", required = true) double energyCost,
                                           @JsonProperty(value = "startEnergy", required = true) double startEnergy) {
        List<IRecipeIngredient> adjustedInput = RecipeUtil.adjustInputCount("fusion", input, output, 2);
        RecipeFusionSolid recipe = new RecipeFusionSolid(adjustedInput, output, duration, energyCost, startEnergy);

        RecipeUtil.validateRecipeIO("fusion", adjustedInput, output);

        return recipe;
    }
}
