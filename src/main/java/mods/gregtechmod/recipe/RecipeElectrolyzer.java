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
                                            @JsonProperty(value = "energyCost") double energyCost) {
        output = RecipeUtil.adjustOutputCount("electrolyzer", output, 4);

        RecipeElectrolyzer recipe = new RecipeElectrolyzer(input, output, cells, duration, Math.max(energyCost, 1));

        if (!RecipeUtil.validateRecipeIO("electrolyzer", input, output)) recipe.invalid = true;

        return recipe;
    }
}
