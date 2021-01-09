package mods.gregtechmod.recipe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.recipe.IRecipePulverizer;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RecipePulverizer extends Recipe<IRecipeIngredient, List<ItemStack>> implements IRecipePulverizer {
    private final int chance;

    private RecipePulverizer(IRecipeIngredient input, List<ItemStack> output, int chance) {
        super(input, output, 3, 300 * input.getCount());
        this.chance = chance;
    }

    public static RecipePulverizer create(IRecipeIngredient input, ItemStack output, int chance) {
        return create(input, Collections.singletonList(output), chance);
    }

    public static RecipePulverizer create(IRecipeIngredient input, ItemStack primaryOutput, ItemStack secondaryOutput) {
        return create(input, Arrays.asList(primaryOutput, secondaryOutput), 10);
    }

    public static RecipePulverizer create(IRecipeIngredient input, ItemStack primaryOutput, ItemStack secondaryOutput, int chance) {
        return create(input, Arrays.asList(primaryOutput, secondaryOutput), chance);
    }

    @JsonCreator
    public static RecipePulverizer create(@JsonProperty(value = "input", required = true) IRecipeIngredient input,
                                          @JsonProperty(value = "output", required = true) List<ItemStack> output,
                                          @JsonProperty(value = "chance", required = true) int chance) {
        if (output.size() > 2) {
            GregTechAPI.logger.error("Tried to add a pulverizer recipe for " + output.stream().map(ItemStack::getTranslationKey).collect(Collectors.joining()) + " with way too many outputs! Reducing the outputs to 2");
            output = output.subList(0, 2);
        }
        return new RecipePulverizer(input, output, chance);
    }

    @Override
    public ItemStack getPrimaryOutput() {
        return this.output.get(0);
    }

    @Override
    public ItemStack getSecondaryOutput() {
        if (this.output.size() < 2) return ItemStack.EMPTY;
        return this.output.get(1);
    }

    @Override
    public int getChance() {
        return this.chance;
    }

    @Override
    public String toString() {
        ItemStack secondaryOutput = this.getSecondaryOutput();
        return "RecipePulverizer{input="+this.input+",output="+this.getPrimaryOutput().toString()+(!secondaryOutput.isEmpty() ? ",secondaryOutput="+secondaryOutput.toString()+",chance="+this.chance : "")+"}";
    }
}
