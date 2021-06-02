package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.slot.SlotInvSlot;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityBasicMachine;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerBasicMachine<T extends TileEntityBasicMachine<?, ?, ?, ?>> extends ContainerMachineBase<T> {

    public ContainerBasicMachine(EntityPlayer player, T base) {
        super(player, base, 166);
        addSlotToContainer(new SlotInvSlot(base.queueInputSlot, 0, 35, 25));
        addSlotToContainer(new SlotInvSlot(base.inputSlot, 0, 53, 25));
        addSlotToContainer(new SlotInvSlot(base.queueOutputSlot, 0, 107, 25));
        addSlotToContainer(new SlotInvSlot(base.outputSlot, 0, 125, 25));
        addSlotToContainer(new SlotInvSlot(base.extraSlot, 0, 80, 63));
    }

    @Override
    protected void getNetworkedFields(List<? super String> list) {
        super.getNetworkedFields(list);
        list.add("provideEnergy");
        list.add("autoOutput");
        list.add("splitInput");
    }
}
