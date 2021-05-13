package mods.gregtechmod.recipe.compat;

import com.fasterxml.jackson.annotation.JsonCreator;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.recipe.Recipe;
import mods.gregtechmod.recipe.util.RecipeUtil;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class RecipeFurnace extends Recipe<IRecipeIngredient, List<ItemStack>> {

    private RecipeFurnace(IRecipeIngredient input, List<ItemStack> output) {
        super(input, output, 100, 3);
    }

    @JsonCreator
    public static RecipeFurnace create(IRecipeIngredient input, ItemStack output) {
        RecipeFurnace recipe = new RecipeFurnace(input, Collections.singletonList(output));

        if (!RecipeUtil.validateRecipeIO("furnace", input, output)) recipe.invalid = true;

        return recipe;
    }

    @Override
    public String toString() {
        return "RecipeSmelting{input="+this.input+",output="+this.output+",duration="+this.duration+",energyCost="+this.energyCost+"}";
    }
}
