package dev.su5ed.gtexperimental.recipe;

import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeSerializers;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeTypes;
import dev.su5ed.gtexperimental.recipe.type.BaseRecipeSerializer;
import dev.su5ed.gtexperimental.recipe.type.MISORecipe;
import dev.su5ed.gtexperimental.recipe.type.MISORecipeType;
import dev.su5ed.gtexperimental.recipe.type.RecipePropertyMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class FusionFluidRecipe extends MISORecipe<FluidStack, FluidStack> {

    public FusionFluidRecipe(ResourceLocation id, List<? extends RecipeIngredient<FluidStack>> inputs, FluidStack output, int duration, double energyCost, double startEnergy) {
        super(ModRecipeTypes.FUSION_FLUID.get(), ModRecipeSerializers.FUSION_FLUID.get(), id, inputs, output, RecipePropertyMap.builder()
            .duration(duration)
            .energyCost(energyCost)
            .startEnergy(startEnergy)
            .build());
    }

    public FusionFluidRecipe(ResourceLocation id, List<? extends RecipeIngredient<FluidStack>> inputs, FluidStack output, RecipePropertyMap properties) {
        super(ModRecipeTypes.FUSION_FLUID.get(), ModRecipeSerializers.FUSION_FLUID.get(), id, inputs, output, properties);
    }

    public static BaseRecipeSerializer<FusionFluidRecipe> createSerializer() {
        return new BaseRecipeSerializer<>(ModRecipeTypes.FUSION_FLUID.get());
    }
}
