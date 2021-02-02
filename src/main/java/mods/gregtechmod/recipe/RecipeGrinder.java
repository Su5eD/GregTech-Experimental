package mods.gregtechmod.recipe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.recipe.IRecipeGrinder;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import mods.gregtechmod.util.RecipeUtil;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class RecipeGrinder extends Recipe<IRecipeIngredient, List<ItemStack>> implements IRecipeGrinder {
    private final IRecipeIngredientFluid fluid;

    private RecipeGrinder(IRecipeIngredient input, IRecipeIngredientFluid fluid, List<ItemStack> output, int duration) {
        super(input, output, 128, duration < 1 ? 100 : duration);
        this.fluid = fluid;
    }

    @JsonCreator
    public static RecipeGrinder create(@JsonProperty(value = "input", required = true) IRecipeIngredient input,
                                       @JsonProperty(value = "fluid", required = true) IRecipeIngredientFluid fluid,
                                       @JsonProperty(value = "output", required = true) List<ItemStack> output,
                                       @JsonProperty(value = "duration") int duration) {
        if (output.size() > 4) {
            GregTechAPI.logger.error("Tried to add a grinder recipe for " + output.stream().map(ItemStack::getTranslationKey).collect(Collectors.joining(", ")) + " with way too many outputs! Reducing them to 3");
            output = output.subList(0, 3);
        }

        RecipeGrinder recipe = new RecipeGrinder(input, fluid, output, duration);

        if (!RecipeUtil.validateRecipeIO("grinder", input, output))
            recipe.invalid = true;
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
