package dev.su5ed.gregtechmod.recipe.gen;

import dev.su5ed.gregtechmod.recipe.setup.ModRecipeSerializers;
import dev.su5ed.gregtechmod.recipe.setup.ModRecipeTypes;
import dev.su5ed.gregtechmod.recipe.type.RecipeIngredient;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public final class ModRecipeBuilders {
    public static MISORecipeBuilder<ItemStack> alloySmelter(List<? extends RecipeIngredient<ItemStack>> inputs, ItemStack output) {
        return new MISORecipeBuilder<>(ModRecipeTypes.ALLOY_SMELTER.get(), ModRecipeSerializers.ALLOY_SMELTER.get(), inputs, output);
    }  
    
    private ModRecipeBuilders() {}
}
