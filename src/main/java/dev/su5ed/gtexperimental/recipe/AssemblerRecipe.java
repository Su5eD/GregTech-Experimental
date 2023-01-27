package dev.su5ed.gtexperimental.recipe;

import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeSerializers;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeTypes;
import dev.su5ed.gtexperimental.recipe.type.BaseRecipeSerializer;
import dev.su5ed.gtexperimental.recipe.type.MISORecipe;
import dev.su5ed.gtexperimental.recipe.type.RecipePropertyMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class AssemblerRecipe extends MISORecipe<ItemStack, ItemStack> {

    public AssemblerRecipe(ResourceLocation id, List<? extends RecipeIngredient<ItemStack>> inputs, ItemStack output, int duration, double energyCost) {
        super(ModRecipeTypes.ASSEMBLER.get(), ModRecipeSerializers.ASSEMBLER.get(), id, inputs, output, duration, energyCost);
    }

    public AssemblerRecipe(ResourceLocation id, List<? extends RecipeIngredient<ItemStack>> inputs, ItemStack output, RecipePropertyMap properties) {
        super(ModRecipeTypes.ASSEMBLER.get(), ModRecipeSerializers.ASSEMBLER.get(), id, inputs, output, properties);
    }
}