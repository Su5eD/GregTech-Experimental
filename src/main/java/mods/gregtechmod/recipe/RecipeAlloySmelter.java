package mods.gregtechmod.recipe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import mods.gregtechmod.api.recipe.IRecipeUniversal;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.recipe.util.RecipeUtil;
import net.minecraft.item.ItemStack;

import java.util.List;

public class RecipeAlloySmelter extends RecipeDualInput implements IRecipeUniversal<List<IRecipeIngredient>> {
    private final boolean universal;

    private RecipeAlloySmelter(List<IRecipeIngredient> input, ItemStack output, int duration, double energyCost, boolean smelting) {
        super(input, output, duration, energyCost);
        this.universal = smelting;
    }

    @JsonCreator
    public static RecipeAlloySmelter create(@JsonProperty(value = "input", required = true) List<IRecipeIngredient> input,
                                            @JsonProperty(value = "output", required = true) ItemStack output,
                                            @JsonProperty(value = "duration", required = true) int duration,
                                            @JsonProperty(value = "energyCost") double energyCost,
                                            @JsonProperty(value = "universal") boolean universal) {
        input = RecipeUtil.adjustInputCount("alloy smelter", input, output, 2);

        RecipeAlloySmelter recipe = new RecipeAlloySmelter(input, output, duration, Math.max(energyCost, 1), universal);

        if (!RecipeUtil.validateRecipeIO("alloy smelter", input, output)) recipe.invalid = true;

        return recipe;
    }

    @Override
    public boolean isUniversal() {
        return this.universal;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("input", input)
                .add("output", output)
                .add("energyCost", energyCost)
                .add("duration", duration)
                .add("universal", universal)
                .toString();
    }
}
