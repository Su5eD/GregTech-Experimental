package mods.gregtechmod.objects.blocks.teblocks.inv;

import ic2.core.block.invslot.InvSlot;
import mods.gregtechmod.inventory.invslot.GtSlotElectricBuffer;

public abstract class TileEntityElectricBufferSingle extends TileEntityElectricBuffer {
    public final GtSlotElectricBuffer buffer;

    public TileEntityElectricBufferSingle(int invSize) {
        this.buffer = new GtSlotElectricBuffer(this, "buffer", getBufferSlotAccess(), invSize);
    }

    protected InvSlot.Access getBufferSlotAccess() {
        return InvSlot.Access.IO;
    }

    @Override
    protected boolean hasItem() {
        return !this.buffer.isEmpty();
    }
}
