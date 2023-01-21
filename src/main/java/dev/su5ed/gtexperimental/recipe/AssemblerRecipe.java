package dev.su5ed.gtexperimental.recipe;

import dev.su5ed.gtexperimental.recipe.setup.ModRecipeSerializers;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeTypes;
import dev.su5ed.gtexperimental.recipe.type.BaseRecipeSerializer;
import dev.su5ed.gtexperimental.recipe.type.MISORecipe;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class AssemblerRecipe extends MISORecipe {

    public AssemblerRecipe(ResourceLocation id, List<? extends RecipeIngredient<ItemStack>> inputs, ItemStack output, int duration, double energyCost) {
        super(ModRecipeTypes.ASSEMBLER.get(), ModRecipeSerializers.ASSEMBLER.get(), id, inputs, output, duration, energyCost);
    }

    public static BaseRecipeSerializer<AssemblerRecipe> createSerializer() {
        return new BaseRecipeSerializer<>(ModRecipeTypes.ASSEMBLER.get());
    }
}
