package mods.gregtechmod.recipe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import mods.gregtechmod.api.recipe.IRecipeCellular;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.recipe.util.RecipeUtil;
import net.minecraft.item.ItemStack;

import java.util.Collection;
import java.util.List;

public class RecipeElectrolyzer extends Recipe<IRecipeIngredient, Collection<ItemStack>> implements IRecipeCellular {
    private final int cells;

    private RecipeElectrolyzer(IRecipeIngredient input, List<ItemStack> output, int cells, int duration, double energyCost) {
        super(input, output, duration, energyCost);
        this.cells = cells;
    }

    @JsonCreator
    public static RecipeElectrolyzer create(@JsonProperty(value = "input", required = true) IRecipeIngredient input,
                                            @JsonProperty(value = "output", required = true) List<ItemStack> output,
                                            @JsonProperty(value = "cells") int cells,
                                            @JsonProperty(value = "duration", required = true) int duration,
                                            @JsonProperty(value = "energyCost") double energyCost) {
        RecipeUtil.adjustOutputCount("electrolyzer", output, 4);

        RecipeElectrolyzer recipe = new RecipeElectrolyzer(input, output, cells, duration, energyCost);

        if (!RecipeUtil.validateRecipeIO("electrolyzer", input, output)) recipe.invalid = true;

        return recipe;
    }

    @Override
    public int getCells() {
        return this.cells;
    }

    @Override
    public String toString() {
        return "RecipeElectrolyzer{input="+this.input+",output="+this.output+",duration="+this.duration+",energyCost="+this.energyCost+",cells="+this.cells+"}";
    }
}
