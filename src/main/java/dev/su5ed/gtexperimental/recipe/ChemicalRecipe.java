package dev.su5ed.gtexperimental.recipe;

import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeSerializers;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeTypes;
import dev.su5ed.gtexperimental.recipe.type.MISORecipe;
import dev.su5ed.gtexperimental.recipe.type.RecipePropertyMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class ChemicalRecipe extends MISORecipe<FluidStack, FluidStack> {

    public ChemicalRecipe(ResourceLocation id, List<? extends RecipeIngredient<FluidStack>> inputs, FluidStack output, int duration) {
        this(id, inputs, output, RecipePropertyMap.builder()
            .duration(duration)
            .build());
    }

    public ChemicalRecipe(ResourceLocation id, List<? extends RecipeIngredient<FluidStack>> inputs, FluidStack output, RecipePropertyMap properties) {
        super(ModRecipeTypes.CHEMICAL.get(), ModRecipeSerializers.CHEMICAL.get(), id, inputs, output, properties);
    }

    @Override
    public double getEnergyCost() {
        return 32;
    }
}
