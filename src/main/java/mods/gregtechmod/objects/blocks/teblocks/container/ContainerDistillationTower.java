package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.slot.SlotInvSlot;
import mods.gregtechmod.objects.blocks.teblocks.struct.TileEntityDistillationTower;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerDistillationTower extends ContainerMachineBase<TileEntityDistillationTower> {

    public ContainerDistillationTower(EntityPlayer player, TileEntityDistillationTower base) {
        super(player, base);
        addSlotToContainer(new SlotInvSlot(base.inputSlot, 0, 62, 41));
        addSlotToContainer(new SlotInvSlot(base.cellSlot, 0, 62, 59));
        addSlotToContainer(new SlotInvSlot(base.outputSlot, 0, 98, 5));
        addSlotToContainer(new SlotInvSlot(base.outputSlot, 1, 98, 23));
        addSlotToContainer(new SlotInvSlot(base.outputSlot, 2, 98, 41));
        addSlotToContainer(new SlotInvSlot(base.outputSlot, 3, 98, 59));
    }
}
