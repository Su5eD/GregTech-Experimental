package mods.gregtechmod.recipe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.recipe.IRecipeCentrifuge;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RecipeCentrifuge extends Recipe<IRecipeIngredient, Collection<ItemStack>> implements IRecipeCentrifuge {
    private final int cells;

    private RecipeCentrifuge(IRecipeIngredient input, List<ItemStack> output, int cells, int duration) {
        super(input, output, 5, duration);
        this.cells = cells;
    }

    @JsonCreator
    public static RecipeCentrifuge create(@JsonProperty(value = "input", required = true) IRecipeIngredient input,
                                          @JsonProperty(value = "output", required = true) List<ItemStack> output,
                                          @JsonProperty(value = "cells") int cells,
                                          @JsonProperty(value = "duration", required = true) int duration) {
        if (output.size() > 4) {
            GregTechAPI.logger.error("Tried to add a centrifuge recipe for " + output.stream().map(ItemStack::getTranslationKey).collect(Collectors.joining(", ")) + " with way too many outputs! Reducing their size to 4");
            output = output.subList(0, 4);
        }

        RecipeCentrifuge recipe = new RecipeCentrifuge(input, output, cells, duration);

        if (input == null) {
            GregTechAPI.logger.error("Tried to add a centrifuge recipe with null input");
            recipe.invalid = true;
        }

        for (ItemStack stack : output) {
            if (stack.isEmpty()) {
                GregTechAPI.logger.error("Tried to add a centrifuge recipe with an empty ItemStack among its outputs");
                recipe.invalid = true;
                break;
            }
        }

        return recipe;
    }

    @Override
    public int getCells() {
        return this.cells;
    }

    @Override
    public String toString() {
        List<String> outputs = this.output.stream()
                .map(Objects::toString)
                .collect(Collectors.toList());
        return "RecipeCentrifuge{input="+this.input+",output=["+String.join(",", outputs)+"],cells="+this.cells+",duration="+this.duration+",energyCost="+this.energyCost+"}";
    }
}
