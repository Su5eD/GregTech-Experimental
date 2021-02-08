package mods.gregtechmod.recipe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import mods.gregtechmod.api.recipe.IRecipeCellular;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.recipe.util.RecipeUtil;
import net.minecraft.item.ItemStack;

import java.util.Collection;
import java.util.List;

public class RecipeCentrifuge extends Recipe<IRecipeIngredient, Collection<ItemStack>> implements IRecipeCellular {
    private final int cells;

    private RecipeCentrifuge(IRecipeIngredient input, List<ItemStack> output, int cells, int duration) {
        super(input, output, duration, 5);
        this.cells = cells;
    }

    @JsonCreator
    public static RecipeCentrifuge create(@JsonProperty(value = "input", required = true) IRecipeIngredient input,
                                          @JsonProperty(value = "output", required = true) List<ItemStack> output,
                                          @JsonProperty(value = "cells") int cells,
                                          @JsonProperty(value = "duration", required = true) int duration) {
        RecipeUtil.adjustOutputCount("centrifuge", output, 4);

        RecipeCentrifuge recipe = new RecipeCentrifuge(input, output, cells, duration);

        if (!RecipeUtil.validateRecipeIO("centrifuge", input, output)) recipe.invalid = true;

        return recipe;
    }

    @Override
    public int getCells() {
        return this.cells;
    }

    @Override
    public String toString() {
        return "RecipeCentrifuge{input="+this.input+",output="+this.output+",duration="+this.duration+",energyCost="+this.energyCost+",cells="+this.cells+"}";
    }
}
