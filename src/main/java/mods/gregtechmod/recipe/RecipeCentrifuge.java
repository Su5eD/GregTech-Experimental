package mods.gregtechmod.recipe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import mods.gregtechmod.api.recipe.CellType;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.recipe.util.RecipeUtil;
import net.minecraft.item.ItemStack;

import java.util.List;

public class RecipeCentrifuge extends RecipeCellular {

    private RecipeCentrifuge(IRecipeIngredient input, List<ItemStack> output, int cells, int duration, CellType cellType) {
        super(input, output, cells, duration, 5, cellType);
    }

    @JsonCreator
    public static RecipeCentrifuge create(@JsonProperty(value = "input", required = true) IRecipeIngredient input,
                                          @JsonProperty(value = "output", required = true) List<ItemStack> output,
                                          @JsonProperty(value = "cells") int cells,
                                          @JsonProperty(value = "duration", required = true) int duration,
                                          @JsonProperty(value = "cellType") CellType cellType) {
        output = RecipeUtil.adjustOutputCount("centrifuge", output, 4);

        RecipeCentrifuge recipe = new RecipeCentrifuge(input, output, cells, duration, cellType == null ? CellType.CELL : cellType);

        if (!RecipeUtil.validateRecipeIO("centrifuge", input, output)) recipe.invalid = true;

        return recipe;
    }
}
