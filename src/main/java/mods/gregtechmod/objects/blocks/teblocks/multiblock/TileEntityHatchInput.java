package mods.gregtechmod.objects.blocks.teblocks.multiblock;

import ic2.core.block.invslot.InvSlotConsumableLiquid;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

public class TileEntityHatchInput extends TileEntityHatchIO {

    public TileEntityHatchInput() {
        super("hatch_input", InvSlotConsumableLiquid.OpType.Drain, true, false, true);
    }
    
    public ItemStack getItem() {
        return this.tank.inputSlot.get();
    }
    
    public FluidStack getFluid() {
        return this.tank.content.getFluid();
    }
    
    public boolean depleteInput(ItemStack stack) {
        if (!stack.isEmpty()) {
            FluidStack fluid = FluidUtil.getFluidContained(stack);
            if (fluid != null) return depleteInput(fluid);
            else {
                ItemStack input = this.tank.inputSlot.get();
                int count = stack.getCount();
                if (input.isItemEqual(stack) && input.getCount() >= count) {
                    this.tank.inputSlot.consume(count);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean depleteInput(FluidStack fluid) {
        FluidStack content = this.tank.content.getFluid();
        if (content != null && content.isFluidEqual(fluid)) {
            FluidStack drainTest = this.tank.content.drain(fluid.amount, false);
            if (drainTest != null && drainTest.amount >= fluid.amount) {
                FluidStack drained = this.tank.content.drain(fluid.amount, true);
                return drained != null && drained.amount >= fluid.amount;
            }
        }
        return false;
    }
}
