package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.slot.SlotInvSlot;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityIndustrialCentrifugeBase;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerIndustrialCentrifuge extends ContainerMachineBase<TileEntityIndustrialCentrifugeBase> {

    public ContainerIndustrialCentrifuge(EntityPlayer player, TileEntityIndustrialCentrifugeBase base) {
        super(player, base, 166);
        addSlotToContainer(new SlotInvSlot(base.cellSlot, 0, 50, 5));
        addSlotToContainer(new SlotInvSlot(base.inputSlot, 0, 80, 35));
        addSlotToContainer(new SlotInvSlot(base.outputSlot, 0, 80, 5));
        addSlotToContainer(new SlotInvSlot(base.outputSlot, 1, 110, 35));
        addSlotToContainer(new SlotInvSlot(base.outputSlot, 2, 80, 65));
        addSlotToContainer(new SlotInvSlot(base.outputSlot, 3, 50, 35));
    }

    @Override
    protected void getNetworkedFields(List<? super String> list) {
        super.getNetworkedFields(list);
        list.add("tank");
    }
}
