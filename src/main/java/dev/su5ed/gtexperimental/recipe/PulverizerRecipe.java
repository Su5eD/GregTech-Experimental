package dev.su5ed.gtexperimental.recipe;

import dev.su5ed.gtexperimental.recipe.setup.ModRecipeSerializers;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeTypes;
import dev.su5ed.gtexperimental.recipe.type.BaseRecipeSerializer;
import dev.su5ed.gtexperimental.recipe.type.RecipeIngredient;
import dev.su5ed.gtexperimental.recipe.type.SIMORecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class PulverizerRecipe extends SIMORecipe {

    public PulverizerRecipe(ResourceLocation id, RecipeIngredient<ItemStack> input, List<ItemStack> outputs) {
        super(ModRecipeTypes.PULVERIZER.get(), ModRecipeSerializers.PULVERIZER.get(), id, input, outputs);
    }

    public static BaseRecipeSerializer<PulverizerRecipe> createSerializer() {
        return new BaseRecipeSerializer<>(ModRecipeTypes.PULVERIZER.get());
    }
}
