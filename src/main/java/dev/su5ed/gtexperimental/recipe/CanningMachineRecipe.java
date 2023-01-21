package dev.su5ed.gtexperimental.recipe;

import dev.su5ed.gtexperimental.recipe.setup.ModRecipeSerializers;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeTypes;
import dev.su5ed.gtexperimental.recipe.type.BaseRecipeSerializer;
import dev.su5ed.gtexperimental.recipe.type.MIMORecipe;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class CanningMachineRecipe extends MIMORecipe {

    public CanningMachineRecipe(ResourceLocation id, List<? extends RecipeIngredient<ItemStack>> inputs, List<ItemStack> outputs, int duration, double energyCost) {
        super(ModRecipeTypes.CANNING_MACHINE.get(), ModRecipeSerializers.CANNING_MACHINE.get(), id, inputs, outputs, duration, energyCost);
    }

    public static BaseRecipeSerializer<CanningMachineRecipe> createSerializer() {
        return new BaseRecipeSerializer<>(ModRecipeTypes.CANNING_MACHINE.get());
    }
}
