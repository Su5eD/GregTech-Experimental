package mods.gregtechmod.recipe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import ic2.api.item.IC2Items;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.recipe.ingredient.RecipeIngredientItemStack;
import mods.gregtechmod.recipe.util.RecipeUtil;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.List;

public class RecipeImplosion extends Recipe<List<IRecipeIngredient>, List<ItemStack>> {

    private RecipeImplosion(IRecipeIngredient input, int tnt, List<ItemStack> output) {
        super(Arrays.asList(input, RecipeIngredientItemStack.create(IC2Items.getItem("te", "itnt"), tnt)), output, 20, 32);
    }

    @JsonCreator
    public static RecipeImplosion create(@JsonProperty(value = "input", required = true) IRecipeIngredient input,
                                         @JsonProperty(value = "tnt", required = true) int tnt,
                                         @JsonProperty(value = "output", required = true) List<ItemStack> output) {
        tnt = tnt > 0 ? Math.min(tnt, 64) : 1;
        output = RecipeUtil.adjustOutputCount("implosion", output, 2);

        RecipeImplosion recipe = new RecipeImplosion(input, tnt, output);

        if (!RecipeUtil.validateRecipeIO("implosion", input, output)) recipe.invalid = true;

        return recipe;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("input", input)
                .add("output", output)
                .add("duration", duration)
                .add("energyCost", energyCost)
                .toString();
    }
}
