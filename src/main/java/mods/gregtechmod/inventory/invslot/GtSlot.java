package mods.gregtechmod.inventory.invslot;

import ic2.core.block.IInventorySlotHolder;
import ic2.core.block.invslot.InvSlot;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityAutoNBT;
import net.minecraft.item.ItemStack;

public class GtSlot extends InvSlot {
    
    public GtSlot(IInventorySlotHolder<?> base, String name, Access access, int count) {
        super(base, name, access, count);
    }

    @Override
    public boolean accepts(ItemStack stack) {
        return this.access.isInput();
    }
    
    @Override
    public void onChanged() {
        ((TileEntityAutoNBT) this.base).onInventoryChanged();
    }
}
