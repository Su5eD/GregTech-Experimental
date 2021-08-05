package mods.gregtechmod.recipe.fuel;

import mods.gregtechmod.api.recipe.fuel.IFuel;
import mods.gregtechmod.api.recipe.fuel.IFuelManagerFluid;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

public class FuelManagerFluid<F extends IFuel<? extends IRecipeIngredient>> extends FuelManagerSolid<F> implements IFuelManagerFluid<F> {
    @Override
    public F getFuel(ItemStack target) {
        FluidStack fluid = FluidUtil.getFluidContained(target);
        if (fluid != null) return getFuel(fluid.getFluid());

        return super.getFuel(target);
    }

    @Override
    public F getFuel(Fluid target) {
        return this.fuels.stream()
                .filter(fuel -> {
                    IRecipeIngredient input = fuel.getInput();
                    if (input instanceof IRecipeIngredientFluid) return ((IRecipeIngredientFluid) input).apply(target);
                    return false;
                })
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean removeFuel(Fluid target) {
        F fuel = getFuel(target);
        return fuel != null && this.fuels.remove(fuel);
    }

    @Override
    public boolean hasFuel(Fluid target) {
        return this.fuels.stream()
                .map(IFuel::getInput)
                .filter(IRecipeIngredientFluid.class::isInstance)
                .map(IRecipeIngredientFluid.class::cast)
                .anyMatch(input -> input.apply(target));
    }
}
