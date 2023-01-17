package dev.su5ed.gregtechmod.recipe;

import dev.su5ed.gregtechmod.recipe.setup.ModRecipeSerializers;
import dev.su5ed.gregtechmod.recipe.setup.ModRecipeTypes;
import dev.su5ed.gregtechmod.recipe.type.BaseRecipeSerializer;
import dev.su5ed.gregtechmod.recipe.type.RecipeIngredient;
import dev.su5ed.gregtechmod.recipe.type.SISORecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class BenderRecipe extends SISORecipe {

    public BenderRecipe(ResourceLocation id, RecipeIngredient<ItemStack> input, ItemStack output, int duration, double energyCost) {
        super(ModRecipeTypes.BENDER.get(), ModRecipeSerializers.BENDER.get(), id, input, output, duration, energyCost);
    }

    public static BaseRecipeSerializer<BenderRecipe> createSerializer() {
        return new BaseRecipeSerializer<>(ModRecipeTypes.BENDER.get());
    }
}
