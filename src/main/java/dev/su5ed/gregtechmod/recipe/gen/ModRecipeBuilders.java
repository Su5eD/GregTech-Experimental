package dev.su5ed.gregtechmod.recipe.gen;

import dev.su5ed.gregtechmod.recipe.AlloySmelterRecipe;
import dev.su5ed.gregtechmod.recipe.type.MISORecipe;
import dev.su5ed.gregtechmod.recipe.type.RecipeIngredient;
import dev.su5ed.gregtechmod.recipe.type.RecipeIngredientType;
import dev.su5ed.gregtechmod.recipe.type.VanillaRecipeIngredient;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public final class ModRecipeBuilders {
    public static AlloySmelterRecipeBuilder alloySmelter(RecipeIngredient<ItemStack> input, ItemStack output, int duration, double energyCost) {
        return alloySmelter(List.of(input), output, duration, energyCost);
    }
    
    public static AlloySmelterRecipeBuilder alloySmelter(RecipeIngredient<ItemStack> primary, RecipeIngredient<ItemStack> secondary, ItemStack output, int duration, double energyCost) {
        return alloySmelter(List.of(primary, secondary), output, duration, energyCost);
    }
    
    public static AlloySmelterRecipeBuilder alloySmelter(List<? extends RecipeIngredient<ItemStack>> inputs, ItemStack output, int duration, double energyCost) {
        MISORecipe recipe = new AlloySmelterRecipe(null, inputs, output, duration, energyCost);
        return new AlloySmelterRecipeBuilder(recipe);
    }  
    
    private ModRecipeBuilders() {}
}
