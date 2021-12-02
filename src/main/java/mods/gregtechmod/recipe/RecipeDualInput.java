package mods.gregtechmod.recipe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
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
        input = RecipeUtil.adjustInputCount("dual input", input, output, 2);

        RecipeDualInput recipe = new RecipeDualInput(input, output, duration, Math.max(energyCost, 1));

        if (!RecipeUtil.validateRecipeIO("dual input", input, output)) recipe.invalid = true;

        return recipe;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("input", input)
                .add("output", output)
                .add("duration", duration)
                .add("energyCost", energyCost)
                .toString();
    }
}
