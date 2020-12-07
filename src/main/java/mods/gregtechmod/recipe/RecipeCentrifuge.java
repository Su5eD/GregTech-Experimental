package mods.gregtechmod.recipe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import mods.gregtechmod.api.recipe.IRecipeCentrifuge;
import net.minecraft.item.ItemStack;

import java.util.Collection;
import java.util.List;

public class RecipeCentrifuge extends Recipe<ItemStack, Collection<ItemStack>> implements IRecipeCentrifuge {
    private final int cells;

    @JsonCreator
    public RecipeCentrifuge(@JsonProperty(value = "input", required = true) ItemStack input,
                            @JsonProperty(value = "output", required = true) List<ItemStack> output,
                            @JsonProperty(value = "duration", required = true) int duration,
                            @JsonProperty(value = "cells") int cells) {
        super(input, output, 5, duration);
        this.cells = cells;
    }

    @Override
    public int getCells() {
        return this.cells;
    }
}
