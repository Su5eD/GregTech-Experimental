package mods.gregtechmod.inventory.invslot;

import ic2.core.block.invslot.InvSlot;
import mods.gregtechmod.objects.blocks.teblocks.inv.TileEntityElectricBuffer;

public class GtSlotElectricBuffer extends InvSlot {

    public GtSlotElectricBuffer(TileEntityElectricBuffer base, String name, Access access, int count) {
        super(base, name, access, count);
    }

    @Override
    public void onChanged() {
        ((TileEntityElectricBuffer) this.base).inventoryModified.reset();
    }
}
