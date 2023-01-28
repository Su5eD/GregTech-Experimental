package dev.su5ed.gtexperimental.recipe;

import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeSerializers;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeTypes;
import dev.su5ed.gtexperimental.recipe.type.RecipePropertyMap;
import dev.su5ed.gtexperimental.recipe.type.SIMORecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class LatheRecipe extends SIMORecipe<ItemStack> {

    public LatheRecipe(ResourceLocation id, RecipeIngredient<ItemStack> input, List<ItemStack> output, int duration, double energyCost) {
        super(ModRecipeTypes.LATHE.get(), ModRecipeSerializers.LATHE.get(), id, input, output, duration, energyCost);
    }

    public LatheRecipe(ResourceLocation id, RecipeIngredient<ItemStack> input, List<ItemStack> outputs, RecipePropertyMap properties) {
        super(ModRecipeTypes.LATHE.get(), ModRecipeSerializers.LATHE.get(), id, input, outputs, properties);
    }
}
