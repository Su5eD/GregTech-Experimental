package mods.gregtechmod.inventory.invslot;

import ic2.core.block.IInventorySlotHolder;
import ic2.core.block.invslot.InvSlotConsumable;
import mods.gregtechmod.compat.ModHandler;
import mods.gregtechmod.core.GregTechMod;
import net.minecraft.item.ItemStack;

public class GtConsumableCell extends InvSlotConsumable {

    public GtConsumableCell(IInventorySlotHolder<?> base, String name, int count) {
        super(base, name, count);
    }

    @Override
    public boolean accepts(ItemStack stack) {
        return GregTechMod.classic && stack.isItemEqual(ModHandler.emptyFuelCan) || stack.isItemEqual(ModHandler.emptyCell) && stack.getTagCompound() == null;
    }
}
