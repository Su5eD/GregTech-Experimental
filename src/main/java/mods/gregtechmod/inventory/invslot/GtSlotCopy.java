package mods.gregtechmod.inventory.invslot;

import ic2.core.block.IInventorySlotHolder;
import ic2.core.block.invslot.InvSlotConsumable;
import mods.gregtechmod.api.recipe.GtRecipes;
import net.minecraft.item.ItemStack;

public class GtSlotCopy extends InvSlotConsumable {

    public GtSlotCopy(IInventorySlotHolder<?> base) {
        super(base, "book", Access.I, 1, InvSide.BOTTOM);
    }

    @Override
    public boolean accepts(ItemStack stack) {
        return GtRecipes.printer.hasRecipeFor(stack);
    }
}
