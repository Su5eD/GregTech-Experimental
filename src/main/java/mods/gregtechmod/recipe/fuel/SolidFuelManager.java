package mods.gregtechmod.recipe.fuel;

import mods.gregtechmod.api.recipe.fuel.IFuel;
import mods.gregtechmod.api.recipe.fuel.IFuelManager;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SolidFuelManager implements IFuelManager<IFuel<IRecipeIngredient, ItemStack>, IRecipeIngredient, ItemStack> {
    private final Set<IFuel<IRecipeIngredient, ItemStack>> fuels = new HashSet<>();

    @Override
    public boolean addFuel(IRecipeIngredient fuel, double energy) {
        return this.fuels.add(new FuelSolid(fuel, energy));
    }

    @Override
    public IFuel<IRecipeIngredient, ItemStack> getFuel(ItemStack target) {
        return this.fuels.stream()
                .filter(fuel -> fuel.getFuel().apply(target))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean removeFuel(ItemStack target) {
        IFuel<IRecipeIngredient, ItemStack> fuel = this.getFuel(target);
        return fuel != null && this.fuels.remove(fuel);
    }

    @Override
    public Collection<IFuel<IRecipeIngredient, ItemStack>> getFuels() {
        return this.fuels;
    }
}
