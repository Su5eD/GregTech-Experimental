package dev.su5ed.gtexperimental.recipe;

import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeSerializers;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeTypes;
import dev.su5ed.gtexperimental.recipe.type.BaseRecipeSerializer;
import dev.su5ed.gtexperimental.recipe.type.MISORecipe;
import dev.su5ed.gtexperimental.recipe.type.ModRecipeProperty;
import dev.su5ed.gtexperimental.recipe.type.RecipePropertyMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class FusionSolidRecipe extends MISORecipe<FluidStack, ItemStack> {

    public FusionSolidRecipe(ResourceLocation id, List<? extends RecipeIngredient<FluidStack>> inputs, ItemStack output, int duration, double energyCost, double startEnergy) {
        super(ModRecipeTypes.FUSION_SOLID.get(), ModRecipeSerializers.FUSION_SOLID.get(), id, inputs, output, RecipePropertyMap.builder()
            .duration(duration)
            .energyCost(energyCost)
            .startEnergy(startEnergy)
            .build());
    }

    public FusionSolidRecipe(ResourceLocation id, List<? extends RecipeIngredient<FluidStack>> inputs, ItemStack output, RecipePropertyMap properties) {
        super(ModRecipeTypes.FUSION_SOLID.get(), ModRecipeSerializers.FUSION_SOLID.get(), id, inputs, output, properties);
    }

    public double getStartEnergy() {
        return this.properties.get(ModRecipeProperty.START_ENERGY);
    }

    public static BaseRecipeSerializer<FusionSolidRecipe> createSerializer() {
        return new BaseRecipeSerializer<>(ModRecipeTypes.FUSION_SOLID.get());
    }
}
