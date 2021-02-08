package mods.gregtechmod.recipe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.recipe.util.RecipeUtil;
import net.minecraft.item.ItemStack;

public class RecipeSimple extends Recipe<IRecipeIngredient, ItemStack> {

    private RecipeSimple(IRecipeIngredient input, ItemStack output, int duration, double energyCost) {
        super(input, output, duration, energyCost);
    }

    @JsonCreator
    public static RecipeSimple create(@JsonProperty(value = "input", required = true) IRecipeIngredient input,
                                      @JsonProperty(value = "output", required = true) ItemStack output,
                                      @JsonProperty(value = "duration", required = true) int duration,
                                      @JsonProperty(value = "energyCost", required = true) double energycost) {
        RecipeSimple recipe = new RecipeSimple(input, output, duration, energycost);

        if (!RecipeUtil.validateRecipeIO("simple", input, output)) recipe.invalid = true;

        return recipe;
    }

    @Override
    public String toString() {
        return "RecipeSimple{input="+this.input+",output="+this.output+",duration="+this.duration+",energyCost="+this.energyCost+"}";
    }
}
