package mods.gregtechmod.objects.blocks.teblocks.fusion;

import ic2.core.block.comp.Fluids;
import ic2.core.block.invslot.InvSlotConsumableLiquid;
import ic2.core.util.Util;
import mods.gregtechmod.inventory.tank.GtFluidTank;
import mods.gregtechmod.objects.blocks.teblocks.component.BasicTank;
import mods.gregtechmod.util.JavaUtil;

public class TileEntityFusionMaterialExtractor extends TileEntityFusionMaterialIO {
    
    @Override
    protected BasicTank createTank(Fluids fluids) {
        return new BasicTank(this, fluids, new GtFluidTank(this, "content", Util.noFacings, Util.allFacings, JavaUtil.alwaysTrue(), 10000), InvSlotConsumableLiquid.OpType.Fill, true);
    }
}
