package mods.gregtechmod.recipe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import mods.gregtechmod.api.recipe.CellType;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.recipe.util.RecipeUtil;
import net.minecraft.item.ItemStack;

import java.util.List;

public class RecipeElectrolyzer extends RecipeCellular {

    private RecipeElectrolyzer(IRecipeIngredient input, List<ItemStack> output, int cells, int duration, double energyCost) {
        super(input, output, cells, duration, energyCost, CellType.CELL);
    }

    @JsonCreator
    public static RecipeElectrolyzer create(@JsonProperty(value = "input", required = true) IRecipeIngredient input,
                                            @JsonProperty(value = "output", required = true) List<ItemStack> output,
                                            @JsonProperty(value = "cells") int cells,
                                            @JsonProperty(value = "duration", required = true) int duration,
                                            @JsonProperty(value = "energyCost", required = true) double energyCost) {
        List<ItemStack> adjustedOutput = RecipeUtil.adjustOutputCount("industrial electrolyzer", output, 4);

        RecipeElectrolyzer recipe = new RecipeElectrolyzer(input, adjustedOutput, cells, duration, energyCost);

        if (!RecipeUtil.validateRecipeIO("industrial electrolyzer", input, adjustedOutput)) recipe.invalid = true;

        return recipe;
    }
}
