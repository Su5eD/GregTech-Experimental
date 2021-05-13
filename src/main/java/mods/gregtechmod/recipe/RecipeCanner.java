package mods.gregtechmod.recipe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.recipe.util.RecipeUtil;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.item.ItemStack;

import java.util.List;

public class RecipeCanner extends Recipe<List<IRecipeIngredient>, List<ItemStack>> {

    private RecipeCanner(List<IRecipeIngredient> input, List<ItemStack> output, int duration, double energyCost) {
        super(input, output, duration, energyCost);
    }

    public static RecipeCanner create(IRecipeIngredient primaryInput, IRecipeIngredient secondaryInput, List<ItemStack> output, int duration, double energyCost) {
        return create(GtUtil.nonNullList(primaryInput, secondaryInput), output, duration, energyCost);
    }

    @JsonCreator
    public static RecipeCanner create(@JsonProperty(value = "input", required = true) List<IRecipeIngredient> input,
                                      @JsonProperty(value = "output", required = true) List<ItemStack> output,
                                      @JsonProperty(value = "duration", required = true) int duration,
                                      @JsonProperty(value = "energyCost") double energyCost) {
        input = RecipeUtil.adjustInputCount("canner", input, output, 2);
        output = RecipeUtil.adjustOutputCount("canner", output, 2);

        RecipeCanner recipe = new RecipeCanner(input, output, duration <= 0 ? 1 : duration, Math.max(energyCost, 1));

        if (!RecipeUtil.validateRecipeIO("canner", input, output)) recipe.invalid = true;

        return recipe;
    }

    @Override
    public String toString() {
        return "RecipeCanner{input="+this.input+",output="+this.output+",duration="+this.duration+",energyCost="+this.energyCost+"}";
    }
}
