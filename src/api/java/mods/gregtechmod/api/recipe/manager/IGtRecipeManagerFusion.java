package mods.gregtechmod.api.recipe.manager;

import mods.gregtechmod.api.recipe.IRecipeFusion;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public interface IGtRecipeManagerFusion extends IGtRecipeManager<List<IRecipeIngredientFluid>, List<FluidStack>, IRecipeFusion> {
    IRecipeFusion getRecipeFor(List<FluidStack> input);

    boolean hasRecipeFor(Fluid input);
}
