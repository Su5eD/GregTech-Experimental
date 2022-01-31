package mods.gregtechmod.recipe.fuel;

import mods.gregtechmod.api.recipe.fuel.IFuel;
import mods.gregtechmod.api.recipe.fuel.IFuelManagerFluid;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import one.util.streamex.StreamEx;

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
                    return input instanceof IRecipeIngredientFluid && ((IRecipeIngredientFluid) input).apply(target);
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
        return StreamEx.of(this.fuels)
                .map(IFuel::getInput)
                .select(IRecipeIngredientFluid.class)
                .anyMatch(input -> input.apply(target));
    }
}
