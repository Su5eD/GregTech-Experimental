package dev.su5ed.gregtechmod.recipe;

import dev.su5ed.gregtechmod.recipe.setup.ModRecipeSerializers;
import dev.su5ed.gregtechmod.recipe.setup.ModRecipeTypes;
import dev.su5ed.gregtechmod.recipe.type.BaseRecipeSerializer;
import dev.su5ed.gregtechmod.recipe.type.MISORecipe;
import dev.su5ed.gregtechmod.recipe.type.RecipeIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class AlloySmelterRecipe extends MISORecipe<ItemStack> {

    public AlloySmelterRecipe(ResourceLocation id, List<? extends RecipeIngredient<ItemStack>> inputs, ItemStack output) {
        super(ModRecipeTypes.ALLOY_SMELTER.get(), ModRecipeSerializers.ALLOY_SMELTER.get(), id, inputs, output);
    }

    public static BaseRecipeSerializer<AlloySmelterRecipe> createSerializer() {
        return new BaseRecipeSerializer<>(ModRecipeTypes.ALLOY_SMELTER.get());
    }
}
