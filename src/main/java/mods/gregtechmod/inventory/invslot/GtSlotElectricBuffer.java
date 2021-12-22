package mods.gregtechmod.inventory.invslot;

import ic2.core.block.invslot.InvSlot;
import mods.gregtechmod.objects.blocks.teblocks.inv.TileEntityElectricBufferSmall;

public class GtSlotElectricBuffer extends InvSlot {

    public GtSlotElectricBuffer(TileEntityElectricBufferSmall base, String name, Access access, int count) {
        super(base, name, access, count);
    }

    @Override
    public void onChanged() {
        ((TileEntityElectricBufferSmall) this.base).inventoryModified.reset();
    }
}
