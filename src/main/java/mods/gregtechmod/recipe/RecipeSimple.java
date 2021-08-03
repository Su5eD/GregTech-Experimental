package mods.gregtechmod.recipe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.recipe.util.RecipeUtil;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class RecipeSimple extends Recipe<IRecipeIngredient, List<ItemStack>> {

    private RecipeSimple(IRecipeIngredient input, ItemStack output, int duration, double energyCost) {
        super(input, Collections.singletonList(output), duration, energyCost);
    }

    @JsonCreator
    public static RecipeSimple create(@JsonProperty(value = "input", required = true) IRecipeIngredient input,
                                      @JsonProperty(value = "output", required = true) ItemStack output,
                                      @JsonProperty(value = "duration", required = true) int duration,
                                      @JsonProperty(value = "energyCost", required = true) double energyCost) {
        RecipeSimple recipe = new RecipeSimple(input, output, duration, energyCost);

        if (!RecipeUtil.validateRecipeIO("simple", input, output)) recipe.invalid = true;

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
