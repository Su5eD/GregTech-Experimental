package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.slot.SlotInvSlot;
import mods.gregtechmod.objects.blocks.teblocks.TileEntityQuantumTank;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerQuantumTank extends ContainerMachineBase<TileEntityQuantumTank> {

    public ContainerQuantumTank(EntityPlayer player, TileEntityQuantumTank base) {
        super(player, base, 166);
        addSlotToContainer(new SlotInvSlot(base.inputSlot, 0, 80, 17));
        addSlotToContainer(new SlotInvSlot(base.outputSlot, 0, 80, 53));
    }

    @Override
    protected void getNetworkedFields(List<? super String> list) {
        super.getNetworkedFields(list);
        list.add("content");
    }
}
