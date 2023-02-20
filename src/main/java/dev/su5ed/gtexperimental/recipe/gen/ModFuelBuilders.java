package dev.su5ed.gtexperimental.recipe.gen;

import com.mojang.datafixers.util.Either;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.recipe.type.RecipePropertyMap;
import dev.su5ed.gtexperimental.recipe.type.SIMORecipe;
import dev.su5ed.gtexperimental.recipe.type.SISORecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public final class ModFuelBuilders {

    public static SISORecipeBuilder<FluidStack, FluidStack> denseLiquid(RecipeIngredient<FluidStack> input, double energy) {
        SISORecipe<FluidStack, FluidStack> recipe = SISORecipe.denseLiquid(null, input, FluidStack.EMPTY, RecipePropertyMap.builder().energy(energy).build());
        return new SISORecipeBuilder<>(recipe);
    }

    public static SISORecipeBuilder<Either<ItemStack, FluidStack>, FluidStack> diesel(RecipeIngredient<Either<ItemStack, FluidStack>> input, double energy) {
        SISORecipe<Either<ItemStack, FluidStack>, FluidStack> recipe = SISORecipe.dieselFuel(null, input, FluidStack.EMPTY, RecipePropertyMap.builder().energy(energy).build());
        return new SISORecipeBuilder<>(recipe);
    }

    public static SISORecipeBuilder<FluidStack, FluidStack> gas(RecipeIngredient<FluidStack> input, FluidStack output, double energy) {
        SISORecipe<FluidStack, FluidStack> recipe = SISORecipe.gasFuel(null, input, output, RecipePropertyMap.builder().energy(energy).build());
        return new SISORecipeBuilder<>(recipe);
    }

    public static SIMORecipeBuilder<Either<ItemStack, FluidStack>, ItemStack> hot(RecipeIngredient<Either<ItemStack, FluidStack>> input, ItemStack first, ItemStack second, ItemStack third, ItemStack fourth, double energy) {
        return hot(input, List.of(first, second, third, fourth), energy);
    }

    public static SIMORecipeBuilder<Either<ItemStack, FluidStack>, ItemStack> hot(RecipeIngredient<Either<ItemStack, FluidStack>> input, List<ItemStack> output, double energy) {
        SIMORecipe<Either<ItemStack, FluidStack>, ItemStack> recipe = SIMORecipe.hotFuel(null, input, output, RecipePropertyMap.builder().energy(energy).build());
        return new SIMORecipeBuilder<>(recipe);
    }

    public static SISORecipeBuilder<Either<ItemStack, FluidStack>, ItemStack> magic(RecipeIngredient<Either<ItemStack, FluidStack>> input, double energy) {
        return magic(input, ItemStack.EMPTY, energy);
    }

    public static SISORecipeBuilder<Either<ItemStack, FluidStack>, ItemStack> magic(RecipeIngredient<Either<ItemStack, FluidStack>> input, ItemStack output, double energy) {
        SISORecipe<Either<ItemStack, FluidStack>, ItemStack> recipe = SISORecipe.magic(null, input, output, RecipePropertyMap.builder().energy(energy).build());
        return new SISORecipeBuilder<>(recipe);
    }

    public static SISORecipeBuilder<FluidStack, FluidStack> plasma(RecipeIngredient<FluidStack> input, double energy) {
        SISORecipe<FluidStack, FluidStack> recipe = SISORecipe.plasma(null, input, FluidStack.EMPTY, RecipePropertyMap.builder().energy(energy).build());
        return new SISORecipeBuilder<>(recipe);
    }

    public static SISORecipeBuilder<FluidStack, FluidStack> steam(RecipeIngredient<FluidStack> input, double energy) {
        SISORecipe<FluidStack, FluidStack> recipe = SISORecipe.steam(null, input, FluidStack.EMPTY, RecipePropertyMap.builder().energy(energy).build());
        return new SISORecipeBuilder<>(recipe);
    }

    private ModFuelBuilders() {}
}
