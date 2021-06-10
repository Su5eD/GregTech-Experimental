package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.block.invslot.InvSlot;
import mods.gregtechmod.objects.blocks.teblocks.TileEntityQuantumTank;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerQuantumTank extends ContainerBasicTank<TileEntityQuantumTank> {

    public ContainerQuantumTank(EntityPlayer player, TileEntityQuantumTank base) {
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
