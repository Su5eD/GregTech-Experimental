package mods.gregtechmod.recipe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.util.GtUtil;
import net.minecraft.item.ItemStack;

import java.util.List;

public class RecipeAssembler extends Recipe<List<IRecipeIngredient>, ItemStack> {

    private RecipeAssembler(List<IRecipeIngredient> input, ItemStack output, double energyCost, int duration) {
        super(input, output, energyCost, duration);
    }

    public static RecipeAssembler create(IRecipeIngredient primaryInput, IRecipeIngredient secondaryInput, ItemStack output, int duration, double energyCost) {
        return create(GtUtil.nonNullList(primaryInput, secondaryInput), output, duration, energyCost);
    }

    @JsonCreator
    public static RecipeAssembler create(@JsonProperty(value = "input", required = true) List<IRecipeIngredient> input,
                                         @JsonProperty(value = "output", required = true) ItemStack output,
                                         @JsonProperty(value = "duration", required = true) int duration,
                                         @JsonProperty(value = "energyCost", required = true) double energyCost) {
        if (input.size() > 2) {
            GregTechAPI.logger.error("Tried to add an assembler recipe for " + output.getTranslationKey() + " with way too many inputs! Reducing them to 2");
            input = input.subList(0, 2);
        }

        RecipeAssembler recipe = new RecipeAssembler(input, output, energyCost, duration);

        if (input.contains(null)) {
            GregTechAPI.logger.error("Tried to add an assembler recipe with a null IRecipeIngrent among its inputs");
            recipe.invalid = true;
        }
        if (output.isEmpty()) {
            recipe.invalid = true;
            GregTechAPI.logger.error("Tried to add an assembler recipe with empty output");
        }

        return recipe;
    }
}
