package mods.gregtechmod.recipe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.recipe.IRecipeIngredient;
import net.minecraft.item.ItemStack;

import java.util.List;

public class RecipeAssembler extends Recipe<List<IRecipeIngredient>, ItemStack> {

    private RecipeAssembler(List<IRecipeIngredient> input, ItemStack output, double energyCost, int duration) {
        super(input, output, energyCost, duration);
    }

    @JsonCreator
    public static RecipeAssembler create(@JsonProperty(value = "input", required = true) List<IRecipeIngredient> input,
                                         @JsonProperty(value = "output", required = true) ItemStack output,
                                         @JsonProperty(value = "energyCost", required = true) double energyCost,
                                         @JsonProperty(value = "duration", required = true) int duration) {
        if (input.size() > 2) {
            GregTechAPI.logger.error("Tried to add an assembler recipe for " + output.getTranslationKey() + " with way too many inputs! Reducing them to 2");
            input = input.subList(0, 2);
        }
        return new RecipeAssembler(input, output, energyCost, duration);
    }
}
