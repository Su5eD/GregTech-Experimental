package dev.su5ed.gtexperimental.recipe;

import com.mojang.datafixers.util.Either;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeSerializers;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeTypes;
import dev.su5ed.gtexperimental.recipe.type.RecipePropertyMap;
import dev.su5ed.gtexperimental.recipe.type.SIMORecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class IndustrialCentrifugeRecipe extends SIMORecipe<Either<ItemStack, FluidStack>> {

    public IndustrialCentrifugeRecipe(ResourceLocation id, RecipeIngredient<Either<ItemStack, FluidStack>> input, List<Either<ItemStack, FluidStack>> outputs, int duration) {
        super(ModRecipeTypes.INDUSTRIAL_CENTRIFUGE.get(), ModRecipeSerializers.INDUSTRIAL_CENTRIFUGE.get(), id, input, outputs, RecipePropertyMap.builder()
            .duration(duration)
            .build());
    }

    public IndustrialCentrifugeRecipe(ResourceLocation id, RecipeIngredient<Either<ItemStack, FluidStack>> input, List<Either<ItemStack, FluidStack>> output, RecipePropertyMap properties) {
        super(ModRecipeTypes.INDUSTRIAL_CENTRIFUGE.get(), ModRecipeSerializers.INDUSTRIAL_CENTRIFUGE.get(), id, input, output, properties);
    }

    @Override
    public double getEnergyCost() {
        return 5;
    }
}
