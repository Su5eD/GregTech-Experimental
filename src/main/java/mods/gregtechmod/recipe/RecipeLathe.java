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
    
    public static RecipeLathe create(IRecipeIngredient input, List<ItemStack> output, int duration) {
        return create(input, output, duration, 8);
    }

    @JsonCreator
    public static RecipeLathe create(@JsonProperty(value = "input", required = true) IRecipeIngredient input,
                                     @JsonProperty(value = "output", required = true) List<ItemStack> output,
                                     @JsonProperty(value = "duration", required = true) int duration,
                                     @JsonProperty(value = "energyCost") double energyCost) {
        List<ItemStack> adjustedOutput = RecipeUtil.adjustOutputCount("lathe", output, 2);
        RecipeLathe recipe = new RecipeLathe(input, adjustedOutput, duration, energyCost <= 0 ? 8 : energyCost);

        if (!RecipeUtil.validateRecipeIO("lathe", input, adjustedOutput)) recipe.invalid = true;

        return recipe;
    }
}
