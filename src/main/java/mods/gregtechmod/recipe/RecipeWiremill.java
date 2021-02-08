package mods.gregtechmod.recipe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.recipe.util.RecipeUtil;
import net.minecraft.item.ItemStack;

public class RecipeWiremill extends Recipe<IRecipeIngredient, ItemStack> {

    private RecipeWiremill(IRecipeIngredient input, ItemStack output, int duration, double energyCost) {
        super(input, output, duration, energyCost);
    }

    @JsonCreator
    public static RecipeWiremill create(@JsonProperty(value = "input", required = true) IRecipeIngredient input,
                                        @JsonProperty(value = "output", required = true) ItemStack output,
                                        @JsonProperty(value = "duration", required = true) int duration,
                                        @JsonProperty(value = "energyCost", required = true) double energycost) {
        RecipeWiremill recipe = new RecipeWiremill(input, output, duration, energycost);

        if (!RecipeUtil.validateRecipeIO("wiremill", input, output)) recipe.invalid = true;

        return recipe;
    }

    @Override
    public String toString() {
        return "RecipeWiremill{input="+this.input+",output="+this.output+",duration="+this.duration+",energyCost="+this.energyCost+"}";
    }
}
