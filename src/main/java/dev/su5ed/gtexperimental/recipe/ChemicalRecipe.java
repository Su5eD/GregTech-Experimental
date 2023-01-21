package dev.su5ed.gtexperimental.recipe;

import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeSerializers;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeTypes;
import dev.su5ed.gtexperimental.recipe.type.BaseRecipeSerializer;
import dev.su5ed.gtexperimental.recipe.type.MISORecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class ChemicalRecipe extends MISORecipe<FluidStack> {

    // TODO Remove energyCost
    public ChemicalRecipe(ResourceLocation id, List<? extends RecipeIngredient<FluidStack>> inputs, FluidStack output, int duration, double energyCost) {
        super(ModRecipeTypes.CHEMICAL.get(), ModRecipeSerializers.CHEMICAL.get(), id, inputs, output, duration, 32);
    }

    public static BaseRecipeSerializer<ChemicalRecipe> createSerializer() {
        return new BaseRecipeSerializer<>(ModRecipeTypes.CHEMICAL.get());
    }
}
