package mods.gregtechmod.inventory.invslot;

import ic2.core.block.IInventorySlotHolder;
import ic2.core.block.invslot.InvSlotConsumable;
import net.minecraft.item.ItemStack;

public class GtSlotConsumable extends InvSlotConsumable {

    public GtSlotConsumable(IInventorySlotHolder<?> base, String name, int count) {
        super(base, name, count);
    }

    @Override
    public boolean accepts(ItemStack stack) {
        return true;
    }
}
