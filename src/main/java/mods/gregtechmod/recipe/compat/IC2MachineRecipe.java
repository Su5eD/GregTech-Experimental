package mods.gregtechmod.recipe.compat;

import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.recipe.Recipe;
import net.minecraft.item.ItemStack;

import java.util.List;

public class IC2MachineRecipe extends Recipe<IRecipeIngredient, List<ItemStack>> {

    public IC2MachineRecipe(IRecipeIngredient input, List<ItemStack> output, int duration, double energyCost) {
        super(input, output, duration, energyCost);
    }

    @Override
    public String toString() {
        return "IC2MachineRecipe{input="+this.input+",output="+this.output+",duration="+this.duration+",energyCost="+this.energyCost+"}";
    }
}
