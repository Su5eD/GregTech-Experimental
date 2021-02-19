package mods.gregtechmod.recipe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.recipe.util.RecipeUtil;
import net.minecraft.item.ItemStack;

public class RecipeVacuumFreezer extends Recipe<IRecipeIngredient, ItemStack> {

    public RecipeVacuumFreezer(IRecipeIngredient input, ItemStack output, int duration) {
        super(input, output, duration, 128);
    }

    @JsonCreator
    public static RecipeVacuumFreezer create(@JsonProperty(value = "input", required = true) IRecipeIngredient input,
                                      @JsonProperty(value = "output", required = true) ItemStack output,
                                      @JsonProperty(value = "duration", required = true) int duration) {
        RecipeVacuumFreezer recipe = new RecipeVacuumFreezer(input, output, duration);

        if (!RecipeUtil.validateRecipeIO("vacuum frezzer", input, output)) recipe.invalid = true;

        return recipe;
    }

    @Override
    public String toString() {
        return "RecipeVacuumFreezer{input="+this.input+",output="+this.output+",duration="+this.duration+",energyCost="+this.energyCost+"}";
    }
}
