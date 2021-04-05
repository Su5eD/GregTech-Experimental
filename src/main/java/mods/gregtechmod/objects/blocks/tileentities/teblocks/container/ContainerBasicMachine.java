package mods.gregtechmod.objects.blocks.tileentities.teblocks.container;

import ic2.core.ContainerFullInv;
import ic2.core.slot.SlotInvSlot;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.base.TileEntityBasicMachine;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerBasicMachine<T extends TileEntityBasicMachine> extends ContainerFullInv<T> {

    public ContainerBasicMachine(EntityPlayer player, T base) {
        super(player, base, 166);
        addSlotToContainer(new SlotInvSlot(base.queueInputSlot, 0, 35, 25));
        addSlotToContainer(new SlotInvSlot(base.inputSlot, 0, 53, 25));
        addSlotToContainer(new SlotInvSlot(base.queueOutputSlot, 0, 107, 25));
        addSlotToContainer(new SlotInvSlot(base.outputSlot, 0, 125, 25));
        addSlotToContainer(new SlotInvSlot(base.dischargeSlot, 0, 80, 63));
    }

    @Override
    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("guiProgress");
        ret.add("provideEnergy");
        ret.add("autoOutput");
        ret.add("splitInput");
        return ret;
    }
}
