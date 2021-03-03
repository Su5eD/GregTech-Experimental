package mods.gregtechmod.recipe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.recipe.util.RecipeUtil;
import net.minecraft.item.ItemStack;

import java.util.Collection;
import java.util.List;

public class RecipeCentrifuge extends RecipeCellular {

    private RecipeCentrifuge(IRecipeIngredient input, Collection<ItemStack> output, int cells, int duration) {
        super(input, output, cells, duration, 5);
    }

    @JsonCreator
    public static RecipeCentrifuge create(@JsonProperty(value = "input", required = true) IRecipeIngredient input,
                                          @JsonProperty(value = "output", required = true) List<ItemStack> output,
                                          @JsonProperty(value = "cells") int cells,
                                          @JsonProperty(value = "duration", required = true) int duration) {
        output = RecipeUtil.adjustOutputCount("centrifuge", output, 4);

        RecipeCentrifuge recipe = new RecipeCentrifuge(input, output, cells, duration);

        if (!RecipeUtil.validateRecipeIO("centrifuge", input, output)) recipe.invalid = true;

        return recipe;
    }
}
