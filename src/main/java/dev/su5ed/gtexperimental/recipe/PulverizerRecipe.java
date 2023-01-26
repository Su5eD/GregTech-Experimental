package dev.su5ed.gtexperimental.recipe;

import dev.su5ed.gtexperimental.recipe.setup.ModRecipeSerializers;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeTypes;
import dev.su5ed.gtexperimental.recipe.type.BaseRecipeSerializer;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.recipe.type.SIMORecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class PulverizerRecipe extends SIMORecipe<ItemStack> {

    public PulverizerRecipe(ResourceLocation id, RecipeIngredient<ItemStack> input, List<ItemStack> outputs, int duration, double energyCost) {
        super(ModRecipeTypes.PULVERIZER.get(), ModRecipeSerializers.PULVERIZER.get(), id, input, outputs, duration, energyCost);
    }

    public static BaseRecipeSerializer<PulverizerRecipe> createSerializer() {
        return new BaseRecipeSerializer<>(ModRecipeTypes.PULVERIZER.get());
    }
}
