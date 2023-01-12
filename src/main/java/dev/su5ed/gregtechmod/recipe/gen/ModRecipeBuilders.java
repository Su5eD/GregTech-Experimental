package dev.su5ed.gregtechmod.recipe.gen;

import dev.su5ed.gregtechmod.recipe.AlloySmelterRecipe;
import dev.su5ed.gregtechmod.recipe.type.MISORecipe;
import dev.su5ed.gregtechmod.recipe.type.RecipeIngredient;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public final class ModRecipeBuilders {
    public static MISORecipeBuilder alloySmelter(List<? extends RecipeIngredient<ItemStack>> inputs, ItemStack output) {
        MISORecipe recipe = new AlloySmelterRecipe(null, inputs, output);
        return new MISORecipeBuilder(recipe);
    }  
    
    private ModRecipeBuilders() {}
}
