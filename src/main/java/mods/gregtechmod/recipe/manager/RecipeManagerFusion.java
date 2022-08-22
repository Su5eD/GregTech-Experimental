package mods.gregtechmod.recipe.manager;

import mods.gregtechmod.api.recipe.IRecipeFusion;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerFusion;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import one.util.streamex.StreamEx;

import java.util.List;
import java.util.stream.Collectors;

public class RecipeManagerFusion<R extends IRecipeFusion<IRecipeIngredientFluid, ?>> extends RecipeManagerMultiInput<R, IRecipeIngredientFluid> implements IGtRecipeManagerFusion<R> {

    @Override
    public R getRecipeForFluid(List<FluidStack> input) {
        return this.recipes.stream()
            .filter(recipe -> recipe.getInput().stream()
                .allMatch(ingredient -> input.stream()
                    .anyMatch(ingredient::apply)))
            .min(this::compareCount)
            .orElse(null);
    }

    @Override
    public boolean hasRecipeForFluids(List<FluidStack> input) {
        return this.recipes.stream()
            .anyMatch(recipe -> recipe.getInput().stream()
                .allMatch(ingredient -> input.stream()
                    .anyMatch(ingredient::apply)));
    }

    @Override
    public boolean hasRecipeFor(Fluid input) {
        return this.recipes.stream()
            .anyMatch(recipe -> recipe.getInput().stream()
                .anyMatch(ingredient -> ingredient.apply(input)));
    }

    @Override
    public boolean hasRecipeFor(List<ItemStack> input) {
        List<FluidStack> fluids = input.stream()
            .map(FluidUtil::getFluidContained)
            .collect(Collectors.toList());
        return !fluids.contains(null) && this.recipes.stream()
            .anyMatch(recipe -> recipe.getInput().stream()
                .allMatch(ingredient -> fluids.stream()
                    .anyMatch(ingredient::apply)));
    }

    @Override
    public R getRecipeFor(List<ItemStack> input) {
        List<FluidStack> fluids = StreamEx.of(input)
            .map(FluidUtil::getFluidContained)
            .toImmutableList();
        return fluids.contains(null) ? null : this.recipes.stream()
            .filter(recipe -> recipe.getInput().stream()
                .allMatch(ingredient -> fluids.stream()
                    .anyMatch(ingredient::apply)))
            .min(this::compareCount)
            .orElseGet(() -> getProvidedRecipe(input));
    }
}
