package mods.gregtechmod.recipe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import mods.gregtechmod.api.recipe.IRecipePrinter;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.recipe.util.RecipeUtil;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class RecipePrinter extends Recipe<List<IRecipeIngredient>, List<ItemStack>> implements IRecipePrinter {
    private final IRecipeIngredient copy;

    private RecipePrinter(List<IRecipeIngredient> input, @Nullable IRecipeIngredient copy, ItemStack output, int duration, double energyCost) {
        super(input, Collections.singletonList(output), duration, energyCost);
        this.copy = copy;
    }

    @JsonCreator
    public static RecipePrinter create(@JsonProperty(value = "input", required = true) List<IRecipeIngredient> input,
                                       @JsonProperty(value = "output", required = true) ItemStack output,
                                       @JsonProperty(value = "duration", required = true) int duration,
                                       @JsonProperty(value = "energyCost") double energyCost) {
        return create(input, null, output, duration, energyCost);
    }

    public static RecipePrinter create(List<IRecipeIngredient> input, IRecipeIngredient copy, ItemStack output, int duration, double energyCost) {
        input = RecipeUtil.adjustInputCount("printer", input, Collections.singletonList(output), 3);

        RecipePrinter recipe = new RecipePrinter(input, copy, output, duration, Math.max(energyCost, 1));

        if (!RecipeUtil.validateRecipeIO("printer", input, Collections.singletonList(output))) recipe.invalid = true;

        return recipe;
    }

    @Override
    public String toString() {
        return "RecipePrinter{input="+this.input+",copy="+this.copy+",output="+this.output+",duration="+this.duration+",energyCost="+this.energyCost+"}";
    }

    @Override
    @Nullable
    public IRecipeIngredient getCopyIngredient() {
        return this.copy;
    }
}