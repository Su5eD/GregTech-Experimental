package mods.gregtechmod.recipe.fuel;

import mods.gregtechmod.api.recipe.fuel.IFuel;
import mods.gregtechmod.api.recipe.fuel.IFuelFactory;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;

import java.util.List;

public class FuelFactory implements IFuelFactory {

    @Override
    public IFuel<IRecipeIngredient> makeFuel(IRecipeIngredient input, ItemStack output, double energy) {
        return FuelSimple.create(input, output, energy);
    }

    @Override
    public IFuel<IRecipeIngredient> makeFuel(IRecipeIngredient input, List<ItemStack> output, double energy) {
        return FuelMulti.create(input, output, energy);
    }
}
