package mods.gregtechmod.recipe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import mods.gregtechmod.api.recipe.IRecipePulverizer;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.util.GtUtil;
import mods.gregtechmod.recipe.util.RecipeUtil;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class RecipePulverizer extends Recipe<IRecipeIngredient, List<ItemStack>> implements IRecipePulverizer {
    private final int chance;
    private final boolean overwrite;

    private RecipePulverizer(IRecipeIngredient input, List<ItemStack> output, int chance, boolean overwrite) {
        super(input, output, 300 * input.getCount(), 3);
        this.chance = chance;
        this.overwrite = overwrite;
    }

    public static RecipePulverizer create(IRecipeIngredient input, ItemStack output) {
        return create(input, Collections.singletonList(output), 10, false);
    }

    public static RecipePulverizer create(IRecipeIngredient input, ItemStack primaryOutput, ItemStack secondaryOutput) {
        return create(input, GtUtil.nonEmptyList(primaryOutput, secondaryOutput), 10, false);
    }

    public static RecipePulverizer create(IRecipeIngredient input, ItemStack primaryOutput, ItemStack secondaryOutput, int chance) {
        return create(input, GtUtil.nonEmptyList(primaryOutput, secondaryOutput), chance, false);
    }

    @JsonCreator
    public static RecipePulverizer create(@JsonProperty(value = "input", required = true) IRecipeIngredient input,
                                          @JsonProperty(value = "output", required = true) List<ItemStack> output,
                                          @JsonProperty(value = "chance") int chance,
                                          @JsonProperty(value = "overwrite") boolean overwrite) {
        output = RecipeUtil.adjustOutputCount("pulverizer", output, 2);

        RecipePulverizer recipe = new RecipePulverizer(input, output, chance < 1 ? 10 : chance, overwrite);

        if (!RecipeUtil.validateRecipeIO("pulverizer", input, output)) recipe.invalid = true;

        return recipe;
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
    public boolean shouldOverwrite() {
        return this.overwrite;
    }

    @Override
    public String toString() {
        ItemStack secondaryOutput = this.getSecondaryOutput();
        return "RecipePulverizer{input="+this.input+",output="+this.getPrimaryOutput().toString()+(!secondaryOutput.isEmpty() ? ",secondaryOutput="+secondaryOutput.toString()+",chance="+this.chance : "")+"duration="+this.duration+",overwrite="+this.overwrite+"}";
    }
}
