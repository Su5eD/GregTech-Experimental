package mods.gregtechmod.recipe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import mods.gregtechmod.api.recipe.IRecipeUniversal;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import mods.gregtechmod.recipe.ingredient.RecipeIngredientFluid;
import mods.gregtechmod.recipe.util.RecipeUtil;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.Arrays;
import java.util.List;

public class RecipeSawmill extends Recipe<List<IRecipeIngredient>, List<ItemStack>> implements IRecipeUniversal<List<IRecipeIngredient>> {
    private final boolean universal;

    public RecipeSawmill(IRecipeIngredient input, IRecipeIngredientFluid water, List<ItemStack> output, boolean universal) {
        super(Arrays.asList(input, water), output, 200 * input.getCount(), 32);
        this.universal = universal;
    }

    @JsonCreator
    public static RecipeSawmill create(@JsonProperty(value = "input", required = true) IRecipeIngredient input,
                                       @JsonProperty(value = "output", required = true) List<ItemStack> output,
                                       @JsonProperty(value = "water") int water,
                                       @JsonProperty(value = "universal") boolean universal) {
        List<ItemStack> adjustedOutput = RecipeUtil.adjustOutputCount("sawmill", output, 2);
        IRecipeIngredientFluid fluid = RecipeIngredientFluid.fromFluid(FluidRegistry.WATER, Math.max(water, 1));
        RecipeSawmill recipe = new RecipeSawmill(input, fluid, adjustedOutput, universal);

        if (!RecipeUtil.validateRecipeIO("sawmill", input, adjustedOutput)) recipe.invalid = true;

        return recipe;
    }

    @Override
    public boolean isUniversal() {
        return this.universal;
    }

    @Override
    protected MoreObjects.ToStringHelper toStringHelper() {
        return super.toStringHelper()
                .add("universal", universal);
    }
}
