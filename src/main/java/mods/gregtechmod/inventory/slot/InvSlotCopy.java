package mods.gregtechmod.inventory.slot;

import ic2.core.block.IInventorySlotHolder;
import ic2.core.block.invslot.InvSlotConsumable;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class InvSlotCopy extends InvSlotConsumable {

    public InvSlotCopy(IInventorySlotHolder<?> base) {
        super(base, "book", Access.I, 1, InvSide.BOTTOM);
    }

    @Override
    public boolean accepts(ItemStack stack) {
        Item item = stack.getItem();
        return item == Items.WRITTEN_BOOK || item == Items.FILLED_MAP;
    }
}
