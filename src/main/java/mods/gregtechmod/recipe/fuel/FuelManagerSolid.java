package mods.gregtechmod.recipe.fuel;

import mods.gregtechmod.api.recipe.fuel.IFuel;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;

public class FuelManagerSolid<F extends IFuel<? extends IRecipeIngredient>> extends FuelManager<F, ItemStack> {

    @Override
    public F getFuel(ItemStack target) {
        return this.fuels.stream()
            .filter(fuel -> fuel.getInput().apply(target))
            .findFirst()
            .orElseGet(() -> getProvidedFuel(target));
    }

    @Override
    public boolean hasFuel(ItemStack target) {
        return this.fuels.stream()
            .anyMatch(fuel -> fuel.getInput().apply(target, false))
            || hasProvidedFuel(target);
    }
}
