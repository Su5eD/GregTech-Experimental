package mods.gregtechmod.recipe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import mods.gregtechmod.api.recipe.IRecipeSawmill;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.recipe.util.RecipeUtil;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class RecipeSawmill extends Recipe<IRecipeIngredient, List<ItemStack>> implements IRecipeSawmill {
    private final FluidStack water;

    public RecipeSawmill(IRecipeIngredient input, List<ItemStack> output, int water) {
        super(input, output, 200 * input.getCount(), 32);
        this.water = new FluidStack(FluidRegistry.WATER, water * Fluid.BUCKET_VOLUME);
    }

    @JsonCreator
    public static RecipeSawmill create(@JsonProperty(value = "input", required = true) IRecipeIngredient input,
                                       @JsonProperty(value = "output", required = true) List<ItemStack> output,
                                       @JsonProperty(value = "water") int water) {
        output = RecipeUtil.adjustOutputCount("sawmill", output, 2);

        RecipeSawmill recipe = new RecipeSawmill(input, output, Math.max(1, water));

        if (!RecipeUtil.validateRecipeIO("sawmill", input, output)) recipe.invalid = true;

        return recipe;
    }

    @Override
    public FluidStack getRequiredWater() {
        return this.water;
    }

    @Override
    public String toString() {
        return "RecipeSawmill{input="+this.input+",output="+this.output+",duration="+this.duration+",water="+this.water.amount+"}";
    }
}
