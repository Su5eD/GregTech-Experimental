package dev.su5ed.gtexperimental.recipe;

import dev.su5ed.gtexperimental.recipe.setup.ModRecipeSerializers;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeTypes;
import dev.su5ed.gtexperimental.recipe.type.BaseRecipeSerializer;
import dev.su5ed.gtexperimental.recipe.type.MISORecipe;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class AlloySmelterRecipe extends MISORecipe {

    public AlloySmelterRecipe(ResourceLocation id, List<? extends RecipeIngredient<ItemStack>> inputs, ItemStack output, int duration, double energyCost) {
        super(ModRecipeTypes.ALLOY_SMELTER.get(), ModRecipeSerializers.ALLOY_SMELTER.get(), id, inputs, output, duration, energyCost);
    }

    public static BaseRecipeSerializer<AlloySmelterRecipe> createSerializer() {
        return new BaseRecipeSerializer<>(ModRecipeTypes.ALLOY_SMELTER.get());
    }
}
