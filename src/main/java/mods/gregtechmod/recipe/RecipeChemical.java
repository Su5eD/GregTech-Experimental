package mods.gregtechmod.recipe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.recipe.util.RecipeUtil;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class RecipeChemical extends Recipe<List<IRecipeIngredient>, List<ItemStack>> {

    public RecipeChemical(List<IRecipeIngredient> input, ItemStack output, int duration) {
        super(input, Collections.singletonList(output), duration, 32);
    }

    @JsonCreator
    public static RecipeChemical create(@JsonProperty(value = "input", required = true) List<IRecipeIngredient> input,
                                        @JsonProperty(value = "output", required = true) ItemStack output,
                                        @JsonProperty(value = "duration", required = true) int duration) {
        RecipeChemical recipe = new RecipeChemical(input, output, duration);

        if (!RecipeUtil.validateRecipeIO("chemical", input, output)) recipe.invalid = true;

        return recipe;
    }
}
