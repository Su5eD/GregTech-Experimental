package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.slot.SlotInvSlot;
import mods.gregtechmod.objects.blocks.teblocks.TileEntityIndustrialElectrolyzer;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityIndustrialCentrifugeBase;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerIndustrialElectrolyzer extends ContainerMachineBase<TileEntityIndustrialCentrifugeBase> {

    public ContainerIndustrialElectrolyzer(EntityPlayer player, TileEntityIndustrialElectrolyzer base) {
        super(player, base);

        addSlotToContainer(new SlotInvSlot(base.cellSlot, 0, 50, 46));
        addSlotToContainer(new SlotInvSlot(base.inputSlot, 0, 80, 46));
        addSlotToContainer(new SlotInvSlot(base.outputSlot, 0, 50, 16));
        addSlotToContainer(new SlotInvSlot(base.outputSlot, 1, 70, 16));
        addSlotToContainer(new SlotInvSlot(base.outputSlot, 2, 90, 16));
        addSlotToContainer(new SlotInvSlot(base.outputSlot, 3, 110, 16));
    }

    @Override
    public void getNetworkedFields(List<? super String> list) {
        super.getNetworkedFields(list);
        list.add("tank");
    }
}
