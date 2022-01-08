package mods.gregtechmod.recipe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.recipe.util.RecipeUtil;
import mods.gregtechmod.util.JavaUtil;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class RecipeDualInput extends Recipe<List<IRecipeIngredient>, List<ItemStack>> {

    protected RecipeDualInput(List<IRecipeIngredient> input, ItemStack output, int duration, double energyCost) {
        super(input, Collections.singletonList(output), duration, energyCost);
    }

    public static RecipeDualInput create(IRecipeIngredient primaryInput, IRecipeIngredient secondaryInput, ItemStack output, int duration, double energyCost) {
        return create(JavaUtil.nonNullList(primaryInput, secondaryInput), output, duration, energyCost);
    }

    @JsonCreator
    public static RecipeDualInput create(@JsonProperty(value = "input", required = true) List<IRecipeIngredient> input,
                                         @JsonProperty(value = "output", required = true) ItemStack output,
                                         @JsonProperty(value = "duration", required = true) int duration,
                                         @JsonProperty(value = "energyCost") double energyCost) {
        List<IRecipeIngredient> adjustedInput = RecipeUtil.adjustInputCount("dual input", input, output, 2);
        RecipeDualInput recipe = new RecipeDualInput(adjustedInput, output, duration, Math.max(energyCost, 1));

        if (!RecipeUtil.validateRecipeIO("dual input", adjustedInput, output)) recipe.invalid = true;

        return recipe;
    }
}
