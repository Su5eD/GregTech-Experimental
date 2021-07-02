package mods.gregtechmod.objects.blocks.teblocks.multiblock;

import ic2.core.block.invslot.InvSlotConsumableLiquid;

public class TileEntityHatchInput extends TileEntityHatchIO {

    public TileEntityHatchInput() {
        super("hatch_input", InvSlotConsumableLiquid.OpType.Drain, true, false);
    }
}
