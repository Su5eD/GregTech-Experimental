package mods.gregtechmod.recipe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.recipe.IRecipeGrinder;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import mods.gregtechmod.recipe.util.RecipeUtil;
import net.minecraft.item.ItemStack;

import java.util.List;

public class RecipeGrinder extends Recipe<IRecipeIngredient, List<ItemStack>> implements IRecipeGrinder {
    private final IRecipeIngredientFluid fluid;

    private RecipeGrinder(IRecipeIngredient input, IRecipeIngredientFluid fluid, List<ItemStack> output) {
        super(input, output, input.getCount() * 100, 128);
        this.fluid = fluid;
    }

    @JsonCreator
    public static RecipeGrinder create(@JsonProperty(value = "input", required = true) IRecipeIngredient input,
                                       @JsonProperty(value = "fluid", required = true) IRecipeIngredientFluid fluid,
                                       @JsonProperty(value = "output", required = true) List<ItemStack> output) {
        output = RecipeUtil.adjustOutputCount("grinder", output, 3);

        RecipeGrinder recipe = new RecipeGrinder(input, fluid, output);

        if (!RecipeUtil.validateRecipeIO("grinder", input, output)) recipe.invalid = true;
        if (fluid == null) {
            GregTechAPI.logger.warn("Tried to add a grinder recipe with a null fluid!");
            recipe.invalid = true;
        }

        return recipe;
    }

    @Override
    public IRecipeIngredientFluid getFluid() {
        return this.fluid;
    }

    @Override
    public String toString() {
        return "RecipeGrinder{input="+this.input+",fluid="+this.fluid+",output="+this.output+",duration="+this.duration+",energyCost="+this.energyCost+"}";
    }
}
