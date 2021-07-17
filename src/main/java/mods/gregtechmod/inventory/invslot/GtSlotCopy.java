package mods.gregtechmod.inventory.invslot;

import ic2.core.block.IInventorySlotHolder;
import ic2.core.block.invslot.InvSlotConsumable;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class GtSlotCopy extends InvSlotConsumable {

    public GtSlotCopy(IInventorySlotHolder<?> base) {
        super(base, "book", Access.I, 1, InvSide.BOTTOM);
    }

    @Override
    public boolean accepts(ItemStack stack) {
        Item item = stack.getItem();
        return item == Items.WRITTEN_BOOK || item == Items.FILLED_MAP;
    }
}
