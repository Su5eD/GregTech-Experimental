package mods.gregtechmod.objects.blocks.teblocks.fusion;

import ic2.core.block.comp.Fluids;
import ic2.core.block.invslot.InvSlotConsumableLiquid;
import ic2.core.util.Util;
import mods.gregtechmod.inventory.tank.GtFluidTank;
import mods.gregtechmod.objects.blocks.teblocks.component.BasicTank;
import mods.gregtechmod.util.JavaUtil;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class TileEntityFusionMaterialExtractor extends TileEntityFusionMaterialInjector {
    
    @Override
    protected BasicTank createTank(Fluids fluids) {
        return new BasicTank(this, fluids, new GtFluidTank(this, "content", Util.noFacings, Util.allFacings, JavaUtil.alwaysTrue(), 10000), InvSlotConsumableLiquid.OpType.Fill, true);
    }
    
    public boolean canAddOutput(ItemStack stack) {
        return this.tank.outputSlot.canAdd(stack);
    }
    
    public boolean canAddOutput(FluidStack stack) {
        return this.tank.content.fill(stack, false) == stack.amount; 
    }
    
    public void addOutput(ItemStack stack) {
        this.tank.outputSlot.add(stack);
    }
    
    public void addOutput(FluidStack stack) {
        this.tank.content.fill(stack, true);
    }
}
