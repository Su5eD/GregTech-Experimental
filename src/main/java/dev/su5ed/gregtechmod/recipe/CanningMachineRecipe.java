package dev.su5ed.gregtechmod.recipe;

import dev.su5ed.gregtechmod.recipe.setup.ModRecipeSerializers;
import dev.su5ed.gregtechmod.recipe.setup.ModRecipeTypes;
import dev.su5ed.gregtechmod.recipe.type.BaseRecipeSerializer;
import dev.su5ed.gregtechmod.recipe.type.MIMORecipe;
import dev.su5ed.gregtechmod.recipe.type.RecipeIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class CanningMachineRecipe extends MIMORecipe {

    public CanningMachineRecipe(ResourceLocation id, List<? extends RecipeIngredient<ItemStack>> inputs, List<ItemStack> outputs) {
        super(ModRecipeTypes.CANNING_MACHINE.get(), ModRecipeSerializers.CANNING_MACHINE.get(), id, inputs, outputs);
    }

    public static BaseRecipeSerializer<CanningMachineRecipe> createSerializer() {
        return new BaseRecipeSerializer<>(ModRecipeTypes.CANNING_MACHINE.get());
    }
}
