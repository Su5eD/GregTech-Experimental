package mods.gregtechmod.recipe.manager;

import mods.gregtechmod.api.recipe.IRecipeFusion;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerFusionFluid;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

import java.util.List;
import java.util.stream.Collectors;

public class RecipeManagerFusionFluid extends RecipeManagerFusion<IRecipeIngredientFluid, FluidStack> implements IGtRecipeManagerFusionFluid {

    @Override
    public IRecipeFusion<IRecipeIngredientFluid, FluidStack> getRecipeForFluid(List<FluidStack> input) {
        return this.recipes.stream()
                .filter(recipe -> recipe.getInput().stream()
                    .allMatch(ingredient -> input.stream()
                        .anyMatch(ingredient::apply)))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean hasRecipeForFluid(List<FluidStack> input) {
        return this.recipes.stream()
                .anyMatch(recipe -> recipe.getInput().stream()
                    .allMatch(ingredient -> input.stream()
                        .anyMatch(ingredient::apply)));
    }

    @Override
    public boolean hasRecipeForFluid(Fluid input) {
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
    public IRecipeFusion<IRecipeIngredientFluid, FluidStack> getRecipeFor(List<ItemStack> input) {
        List<FluidStack> fluids = input.stream()
                .map(FluidUtil::getFluidContained)
                .collect(Collectors.toList());
        return fluids.contains(null) ? null : this.recipes.stream()
                .filter(recipe -> recipe.getInput().stream()
                        .allMatch(ingredient -> fluids.stream()
                                .anyMatch(ingredient::apply)))
                .findFirst()
                .orElse(null);
    }
}
