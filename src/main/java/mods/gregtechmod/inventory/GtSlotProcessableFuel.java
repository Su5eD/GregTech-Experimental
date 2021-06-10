package mods.gregtechmod.inventory;

import ic2.core.block.IInventorySlotHolder;
import ic2.core.block.invslot.InvSlotConsumableLiquid;
import mods.gregtechmod.api.recipe.fuel.IFuel;
import mods.gregtechmod.api.recipe.fuel.IFuelManager;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;

public class GtSlotProcessableFuel extends InvSlotConsumableLiquid {
    private final IFuelManager<? extends IFuel<IRecipeIngredient, ?>, ItemStack> fuelManager;
    
    public GtSlotProcessableFuel(IInventorySlotHolder<?> parent, String name, IFuelManager<? extends IFuel<IRecipeIngredient, ?>, ItemStack> fuelManager) {
        super(parent, name, Access.I, 1, InvSide.TOP, OpType.Both);
        this.fuelManager = fuelManager;
    }

    @Override
    public boolean accepts(ItemStack stack) {
        return this.fuelManager.getFuel(stack) != null;
    }
}
