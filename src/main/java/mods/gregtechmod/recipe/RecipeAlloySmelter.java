package mods.gregtechmod.recipe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import mods.gregtechmod.api.recipe.IRecipeAlloySmelter;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.recipe.util.RecipeUtil;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class RecipeAlloySmelter extends RecipeDualInput implements IRecipeAlloySmelter {
    private final boolean universal;

    private RecipeAlloySmelter(List<IRecipeIngredient> input, ItemStack output, int duration, double energyCost, boolean smelting) {
        super(input, output, duration, energyCost);
        this.universal = smelting;
    }

    @JsonCreator
    public static RecipeAlloySmelter create(@JsonProperty(value = "input", required = true) List<IRecipeIngredient> input,
                                            @JsonProperty(value = "output", required = true) ItemStack output,
                                            @JsonProperty(value = "duration", required = true) int duration,
                                            @JsonProperty(value = "energyCost", required = true) double energyCost,
                                            @JsonProperty(value = "universal") boolean universal) {
        input = RecipeUtil.adjustInputCount("alloy smelter", input, Collections.singletonList(output), 2);

        RecipeAlloySmelter recipe = new RecipeAlloySmelter(input, output, duration, energyCost, universal);

        if (!RecipeUtil.validateRecipeIO("alloy smelter", input, Collections.singletonList(output))) recipe.invalid = true;

        return recipe;
    }

    @Override
    public boolean isUniversal() {
        return this.universal;
    }

    @Override
    public String toString() {
        return "RecipeAlloySmelter{input="+this.input+",output="+this.output+",duration="+this.duration+",energyCost="+this.energyCost+",universal="+this.universal+"}";
    }
}
