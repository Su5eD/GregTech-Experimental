package mods.gregtechmod.recipe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import mods.gregtechmod.api.recipe.CellType;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.recipe.util.RecipeUtil;
import net.minecraft.item.ItemStack;

import java.util.List;

public class RecipeDistillation extends RecipeCellular {

    private RecipeDistillation(IRecipeIngredient input, List<ItemStack> output, int cells, int duration) {
        super(input, output, cells, duration, 16, CellType.CELL);
    }

    @JsonCreator
    public static RecipeDistillation create(@JsonProperty(value = "input", required = true) IRecipeIngredient input,
                                            @JsonProperty(value = "output", required = true) List<ItemStack> output,
                                            @JsonProperty(value = "cells") int cells,
                                            @JsonProperty(value = "duration", required = true) int duration) {
        output = RecipeUtil.adjustOutputCount("distillation", output, 4);

        RecipeDistillation recipe = new RecipeDistillation(input, output, cells, duration);

        if (!RecipeUtil.validateRecipeIO("distillation", input, output)) recipe.invalid = true;

        return recipe;
    }
}
