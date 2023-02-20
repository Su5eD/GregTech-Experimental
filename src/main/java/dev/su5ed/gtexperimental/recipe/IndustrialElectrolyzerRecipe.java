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

public class IndustrialElectrolyzerRecipe extends SIMORecipe<Either<ItemStack, FluidStack>, Either<ItemStack, FluidStack>> {

    public IndustrialElectrolyzerRecipe(ResourceLocation id, RecipeIngredient<Either<ItemStack, FluidStack>> input, List<Either<ItemStack, FluidStack>> output, int duration, double energyCost) {
        super(ModRecipeTypes.INDUSTRIAL_CENTRIFUGE.get(), ModRecipeSerializers.INDUSTRIAL_CENTRIFUGE.get(), id, input, output, duration, energyCost);
    }

    public IndustrialElectrolyzerRecipe(ResourceLocation id, RecipeIngredient<Either<ItemStack, FluidStack>> input, List<Either<ItemStack, FluidStack>> output, RecipePropertyMap properties) {
        super(ModRecipeTypes.INDUSTRIAL_ELECTROLYZER.get(), ModRecipeSerializers.INDUSTRIAL_ELECTROLYZER.get(), id, input, output, properties);
    }
}
