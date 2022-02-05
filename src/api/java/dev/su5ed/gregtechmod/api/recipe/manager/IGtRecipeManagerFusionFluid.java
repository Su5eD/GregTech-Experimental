package dev.su5ed.gregtechmod.api.recipe.manager;

import dev.su5ed.gregtechmod.api.recipe.IRecipeFusion;
import dev.su5ed.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public interface IGtRecipeManagerFusionFluid extends IGtRecipeManager<List<IRecipeIngredientFluid>, List<ItemStack>, IRecipeFusion<IRecipeIngredientFluid, FluidStack>> {
    IRecipeFusion<IRecipeIngredientFluid, FluidStack> getRecipeForFluid(List<FluidStack> input);

    boolean hasRecipeForFluid(List<FluidStack> input);

    boolean hasRecipeForFluid(Fluid input);
}
