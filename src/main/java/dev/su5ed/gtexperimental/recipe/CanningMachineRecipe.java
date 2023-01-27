package dev.su5ed.gtexperimental.recipe;

import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeSerializers;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeTypes;
import dev.su5ed.gtexperimental.recipe.type.MIMORecipe;
import dev.su5ed.gtexperimental.recipe.type.RecipePropertyMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class CanningMachineRecipe extends MIMORecipe {

    public CanningMachineRecipe(ResourceLocation id, List<? extends RecipeIngredient<ItemStack>> inputs, List<ItemStack> output, int duration, double energyCost) {
        super(ModRecipeTypes.CANNING_MACHINE.get(), ModRecipeSerializers.CANNING_MACHINE.get(), id, inputs, output, duration, energyCost);
    }

    public CanningMachineRecipe(ResourceLocation id, List<? extends RecipeIngredient<ItemStack>> inputs, List<ItemStack> outputs, RecipePropertyMap properties) {
        super(ModRecipeTypes.CANNING_MACHINE.get(), ModRecipeSerializers.CANNING_MACHINE.get(), id, inputs, outputs, properties);
    }
}
