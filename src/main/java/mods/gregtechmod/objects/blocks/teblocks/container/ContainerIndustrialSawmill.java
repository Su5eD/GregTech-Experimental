package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.slot.SlotInvSlot;
import mods.gregtechmod.objects.blocks.teblocks.struct.TileEntityIndustrialSawmill;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerIndustrialSawmill extends ContainerMachineBase<TileEntityIndustrialSawmill> {

    public ContainerIndustrialSawmill(EntityPlayer player, TileEntityIndustrialSawmill base) {
        super(player, base);

        addSlotToContainer(new SlotInvSlot(base.inputSlot, 0, 34, 16));
        addSlotToContainer(new SlotInvSlot(base.secondaryInput, 0, 34, 34));
        addSlotToContainer(new SlotInvSlot(base.outputSlot, 0, 86, 25));
        addSlotToContainer(new SlotInvSlot(base.outputSlot, 1, 104, 25));
        addSlotToContainer(new SlotInvSlot(base.fluidContainerOutput, 0, 122, 25));
    }

    @Override
    public void getNetworkedFields(List<? super String> list) {
        super.getNetworkedFields(list);
        list.add("waterTank");
    }
}
