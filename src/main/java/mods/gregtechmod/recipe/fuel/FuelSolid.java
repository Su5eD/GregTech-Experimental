package mods.gregtechmod.recipe.fuel;

import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;

public class FuelSolid extends Fuel<IRecipeIngredient, ItemStack> {

    public FuelSolid(IRecipeIngredient fuel, double energy) {
        super(fuel, energy);
    }

    @Override
    public boolean apply(ItemStack fuel) {
        return this.fuel.apply(fuel);
    }
}
