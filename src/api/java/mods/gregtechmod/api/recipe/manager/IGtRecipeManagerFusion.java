package mods.gregtechmod.api.recipe.manager;

import mods.gregtechmod.api.recipe.IRecipeFusion;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public interface IGtRecipeManagerFusion<R extends IRecipeFusion<IRecipeIngredientFluid, ?>> extends IGtRecipeManager<List<IRecipeIngredientFluid>, List<ItemStack>, R> {
    R getRecipeForFluid(List<FluidStack> input);

    boolean hasRecipeForFluids(List<FluidStack> input);

    boolean hasRecipeFor(Fluid input);
}
