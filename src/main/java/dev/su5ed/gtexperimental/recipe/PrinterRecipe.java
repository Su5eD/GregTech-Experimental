package dev.su5ed.gtexperimental.recipe;

import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeSerializers;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeTypes;
import dev.su5ed.gtexperimental.recipe.type.MISORecipe;
import dev.su5ed.gtexperimental.recipe.type.RecipePropertyMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class PrinterRecipe extends MISORecipe<ItemStack, ItemStack> {

    public PrinterRecipe(ResourceLocation id, List<? extends RecipeIngredient<ItemStack>> inputs, ItemStack output, int duration, double energyCost) {
        super(ModRecipeTypes.PRINTER.get(), ModRecipeSerializers.PRINTER.get(), id, inputs, output, duration, energyCost);
    }

    public PrinterRecipe(ResourceLocation id, List<? extends RecipeIngredient<ItemStack>> inputs, ItemStack output, RecipePropertyMap properties) {
        super(ModRecipeTypes.PRINTER.get(), ModRecipeSerializers.PRINTER.get(), id, inputs, output, properties);
    }
}
