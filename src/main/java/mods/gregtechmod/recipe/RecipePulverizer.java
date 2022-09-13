package mods.gregtechmod.recipe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import mods.gregtechmod.api.recipe.IRecipePulverizer;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.recipe.util.RecipeUtil;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class RecipePulverizer extends Recipe<IRecipeIngredient, List<ItemStack>> implements IRecipePulverizer {
    public static final int DEFAULT_CHANCE = 10;

    private final int chance;
    private final boolean overwrite;
    private final boolean universal;

    private RecipePulverizer(IRecipeIngredient input, List<ItemStack> output, double energyCost, int chance, boolean overwrite, boolean universal) {
        super(input, output, 300 * input.getCount(), energyCost);
        this.chance = chance;
        this.overwrite = overwrite;
        this.universal = universal;
    }

    public static RecipePulverizer create(IRecipeIngredient input, ItemStack output) {
        return create(input, Collections.singletonList(output), DEFAULT_CHANCE, false);
    }

    public static RecipePulverizer create(IRecipeIngredient input, ItemStack primaryOutput, ItemStack secondaryOutput) {
        return create(input, GtUtil.nonEmptyList(primaryOutput, secondaryOutput), DEFAULT_CHANCE, false);
    }

    public static RecipePulverizer create(IRecipeIngredient input, ItemStack primaryOutput, ItemStack secondaryOutput, int chance) {
        return create(input, GtUtil.nonEmptyList(primaryOutput, secondaryOutput), chance);
    }

    public static RecipePulverizer create(IRecipeIngredient input, List<ItemStack> output, int chance) {
        return create(input, output, chance, false, true);
    }

    public static RecipePulverizer create(IRecipeIngredient input, List<ItemStack> output, int chance, boolean overwrite) {
        return create(input, output, chance, overwrite, true);
    }

    @JsonCreator
    public static RecipePulverizer create(@JsonProperty(value = "input", required = true) IRecipeIngredient input,
                                          @JsonProperty(value = "output", required = true) List<ItemStack> output,
                                          @JsonProperty(value = "chance") int chance,
                                          @JsonProperty(value = "overwrite") boolean overwrite,
                                          @JsonProperty(value = "universal") Boolean universal) {
        return create(input, output, 3, chance, overwrite, universal == null || universal);
    }

    public static RecipePulverizer create(IRecipeIngredient input, List<ItemStack> output, double energyCost, int chance, boolean overwrite, boolean universal) {
        List<ItemStack> adjustedOutput = RecipeUtil.adjustOutputCount("pulverizer", output, 2);
        RecipePulverizer recipe = new RecipePulverizer(input, adjustedOutput, energyCost, chance < 1 ? DEFAULT_CHANCE : chance, overwrite, universal);

        if (!RecipeUtil.validateRecipeIO("pulverizer", input, adjustedOutput)) recipe.invalid = true;

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
    public boolean isUniversal() {
        return this.universal;
    }

    @Override
    protected MoreObjects.ToStringHelper toStringHelper() {
        MoreObjects.ToStringHelper helper = super.toStringHelper();
        ItemStack secondaryOutput = getSecondaryOutput();

        if (!secondaryOutput.isEmpty()) {
            helper.add("secondaryOutput", secondaryOutput)
                .add("chance", chance);
        }

        return helper
            .add("overwrite", overwrite)
            .add("universal", universal);
    }
}
