package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.block.invslot.InvSlot;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityFluidGenerator;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerFluidGenerator extends ContainerBasicTank<TileEntityFluidGenerator> {

    public ContainerFluidGenerator(EntityPlayer player, TileEntityFluidGenerator base) {
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

    @Override
    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("solidFuelEnergy");
        return ret;
    }
}
