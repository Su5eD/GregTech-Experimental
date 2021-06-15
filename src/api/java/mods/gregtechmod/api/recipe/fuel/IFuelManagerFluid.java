package mods.gregtechmod.api.recipe.fuel;

import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

public interface IFuelManagerFluid<F extends IFuel<IRecipeIngredient>> extends IFuelManager<F, ItemStack> {
    F getFuel(Fluid target);

    boolean removeFuel(Fluid target);
}
