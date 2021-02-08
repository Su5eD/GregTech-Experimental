package mods.gregtechmod.recipe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.recipe.util.RecipeUtil;
import net.minecraft.item.ItemStack;

import java.util.List;

public class RecipeLathe extends Recipe<IRecipeIngredient, List<ItemStack>> {

    private RecipeLathe(IRecipeIngredient input, List<ItemStack> output, int duration, double energyCost) {
        super(input, output, duration, energyCost);
    }

    @JsonCreator
    public static RecipeLathe create(@JsonProperty(value = "input", required = true) IRecipeIngredient input,
                                     @JsonProperty(value = "output", required = true) List<ItemStack> output,
                                     @JsonProperty(value = "duration", required = true) int duration,
                                     @JsonProperty(value = "energyCost", required = true) double energycost) {
        RecipeLathe recipe = new RecipeLathe(input, output, duration, energycost);

        if (!RecipeUtil.validateRecipeIO("lathe", input, output)) recipe.invalid = true;

        return recipe;
    }

    @Override
    public String toString() {
        return "RecipeLathe{input="+this.input+",output="+this.output+",duration="+this.duration+",energyCost="+this.energyCost+"}";
    }
}
