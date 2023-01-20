package dev.su5ed.gtexperimental.api.recipe.fuel;

import dev.su5ed.gtexperimental.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;

public interface IFuelManagerFluid<F extends IFuel<? extends IRecipeIngredient>> extends IFuelManager<F, ItemStack> {
    F getFuel(Fluid target);

    boolean removeFuel(Fluid target);
    
    boolean hasFuel(Fluid target);
}
