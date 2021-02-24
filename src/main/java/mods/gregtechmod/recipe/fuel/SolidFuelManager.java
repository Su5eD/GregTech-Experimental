package mods.gregtechmod.recipe.fuel;

import mods.gregtechmod.api.recipe.fuel.IFuel;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;

public class SolidFuelManager extends FuelManager<IRecipeIngredient, ItemStack> {

    @Override
    public IFuel<IRecipeIngredient, ItemStack> getFuel(ItemStack target) {
        return this.fuels.stream()
                .filter(fuel -> fuel.getInput().apply(target))
                .findFirst()
                .orElse(null);
    }
}
