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
    private final boolean universal;

    public RecipeSawmill(IRecipeIngredient input, List<ItemStack> output, int water, boolean universal) {
        super(input, output, 200 * input.getCount(), 32);
        this.water = new FluidStack(FluidRegistry.WATER, water * Fluid.BUCKET_VOLUME);
        this.universal = universal;
    }

    @JsonCreator
    public static RecipeSawmill create(@JsonProperty(value = "input", required = true) IRecipeIngredient input,
                                       @JsonProperty(value = "output", required = true) List<ItemStack> output,
                                       @JsonProperty(value = "water") int water,
                                       @JsonProperty(value = "universal") boolean universal) {
        output = RecipeUtil.adjustOutputCount("sawmill", output, 2);

        RecipeSawmill recipe = new RecipeSawmill(input, output, Math.max(1, water), universal);

        if (!RecipeUtil.validateRecipeIO("sawmill", input, output)) recipe.invalid = true;

        return recipe;
    }

    @Override
    public FluidStack getRequiredWater() {
        return this.water;
    }

    @Override
    public boolean isUniversal() {
        return this.universal;
    }

    @Override
    public String toString() {
        return "RecipeSawmill{input="+this.input+",output="+this.output+",duration="+this.duration+",water="+this.water.amount+"}";
    }
}
