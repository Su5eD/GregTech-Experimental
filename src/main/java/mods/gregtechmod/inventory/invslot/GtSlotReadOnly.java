package mods.gregtechmod.inventory.invslot;

import ic2.core.block.IInventorySlotHolder;
import ic2.core.block.invslot.InvSlot;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.Iterator;

/**
 * An InvSlot that is excluded from machine drops
 */
public class GtSlotReadOnly extends InvSlot {

    public GtSlotReadOnly(IInventorySlotHolder<?> base, String name, Access access, int count) {
        super(base, name, access, count);
    }

    @Override
    public Iterator<ItemStack> iterator() {
        // Lazy way exclude ourselves from TileEntityInventory#getAuxDrops
        return Collections.emptyIterator();
    }
}
