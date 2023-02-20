package dev.su5ed.gtexperimental.recipe;

import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeSerializers;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeTypes;
import dev.su5ed.gtexperimental.recipe.type.RecipePropertyMap;
import dev.su5ed.gtexperimental.recipe.type.SISORecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class BenderRecipe extends SISORecipe<ItemStack, ItemStack> {

    public BenderRecipe(ResourceLocation id, RecipeIngredient<ItemStack> input, ItemStack output, int duration, double energyCost) {
        super(ModRecipeTypes.BENDER.get(), ModRecipeSerializers.BENDER.get(), id, input, output, duration, energyCost);
    }

    public BenderRecipe(ResourceLocation id, RecipeIngredient<ItemStack> input, ItemStack output, RecipePropertyMap properties) {
        super(ModRecipeTypes.BENDER.get(), ModRecipeSerializers.BENDER.get(), id, input, output, properties);
    }
}
