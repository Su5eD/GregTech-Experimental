package dev.su5ed.gtexperimental.recipe;

import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeSerializers;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeTypes;
import dev.su5ed.gtexperimental.recipe.type.RecipePropertyMap;
import dev.su5ed.gtexperimental.recipe.type.SISORecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class WiremillRecipe extends SISORecipe<ItemStack, ItemStack> {

    public WiremillRecipe(ResourceLocation id, RecipeIngredient<ItemStack> input, ItemStack output, int duration, double energyCost) {
        super(ModRecipeTypes.WIREMILL.get(), ModRecipeSerializers.WIREMILL.get(), id, input, output, duration, energyCost);
    }

    public WiremillRecipe(ResourceLocation id, RecipeIngredient<ItemStack> input, ItemStack output, RecipePropertyMap properties) {
        super(ModRecipeTypes.WIREMILL.get(), ModRecipeSerializers.WIREMILL.get(), id, input, output, properties);
    }
}
