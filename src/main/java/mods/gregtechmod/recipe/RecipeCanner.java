package mods.gregtechmod.recipe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.recipe.ingredient.RecipeIngredientOre;
import mods.gregtechmod.recipe.util.RecipeUtil;
import mods.gregtechmod.util.JavaUtil;
import mods.gregtechmod.util.OreDictUnificator;
import net.minecraft.item.ItemStack;
import one.util.streamex.StreamEx;

import java.util.List;

public class RecipeCanner extends Recipe<List<IRecipeIngredient>, List<ItemStack>> {

    private RecipeCanner(List<IRecipeIngredient> input, List<ItemStack> output, int duration, double energyCost) {
        super(input, output, duration, energyCost);
    }

    public static RecipeCanner create(IRecipeIngredient primaryInput, IRecipeIngredient secondaryInput, List<ItemStack> output, int duration, double energyCost) {
        return create(JavaUtil.nonNullList(primaryInput, secondaryInput), output, duration, energyCost);
    }

    @JsonCreator
    public static RecipeCanner create(@JsonProperty(value = "input", required = true) List<IRecipeIngredient> input,
                                      @JsonProperty(value = "output", required = true) List<ItemStack> output,
                                      @JsonProperty(value = "duration", required = true) int duration,
                                      @JsonProperty(value = "energyCost") double energyCost) {
        List<IRecipeIngredient> adjustedInput = filterOreDictInput(RecipeUtil.adjustInputCount("canner", input, output, 2), output.get(0));
        List<ItemStack> adjustedOutput = RecipeUtil.adjustOutputCount("canner", output, 2);

        RecipeCanner recipe = new RecipeCanner(adjustedInput, adjustedOutput, Math.max(1, duration), Math.max(1, energyCost));

        if (!RecipeUtil.validateRecipeIO("canner", adjustedInput, adjustedOutput)) recipe.invalid = true;

        return recipe;
    }

    private static List<IRecipeIngredient> filterOreDictInput(List<IRecipeIngredient> input, ItemStack filter) {
        return StreamEx.of(input)
            .map(ingredient -> {
                if (ingredient instanceof RecipeIngredientOre) {
                    List<String> ores = ((RecipeIngredientOre) ingredient).asIngredient().getOres();
                    
                    for (String ore : ores) {
                        if (OreDictUnificator.isItemInstanceOf(filter, ore, false)) {
                            return RecipeIngredientOre.create(ores, ingredient.getCount(), filter);
                        }
                    }
                }
                return ingredient;
            })
            .toImmutableList();
    }
}
