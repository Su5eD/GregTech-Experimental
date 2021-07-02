package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.block.invslot.InvSlot;
import mods.gregtechmod.objects.blocks.teblocks.multiblock.TileEntityHatchIO;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerHatchIO extends ContainerBasicTank<TileEntityHatchIO> {
    
    public ContainerHatchIO(EntityPlayer player, TileEntityHatchIO base) {
        super(player, base);
    }

    @Override
    protected InvSlot getInputSlot() {
        return this.base.tank.inputSlot;
    }

    @Override
    protected InvSlot getOutputSlot() {
        return this.base.tank.outputSlot;
    }
}
