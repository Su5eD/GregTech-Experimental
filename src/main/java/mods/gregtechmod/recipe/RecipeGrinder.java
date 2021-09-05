package mods.gregtechmod.recipe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.recipe.util.RecipeUtil;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.List;

public class RecipeGrinder extends Recipe<List<IRecipeIngredient>, List<ItemStack>> {

    private RecipeGrinder(IRecipeIngredient input, IRecipeIngredientFluid fluid, List<ItemStack> output) {
        super(Arrays.asList(input, fluid), output, input.getCount() * 100, 128);
    }

    @JsonCreator
    public static RecipeGrinder create(@JsonProperty(value = "input", required = true) IRecipeIngredient input,
                                       @JsonProperty(value = "fluid", required = true) IRecipeIngredientFluid fluid,
                                       @JsonProperty(value = "output", required = true) List<ItemStack> output) {
        output = RecipeUtil.adjustOutputCount("industrial grinder", output, 3);

        RecipeGrinder recipe = new RecipeGrinder(input, fluid, output);

        if (!RecipeUtil.validateRecipeIO("industrial grinder", input, output)) recipe.invalid = true;
        if (fluid == null) {
            GregTechMod.LOGGER.warn("Tried to add an industrial grinder recipe with a null fluid!");
            recipe.invalid = true;
        }

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
