package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.slot.SlotInvSlot;
import mods.gregtechmod.objects.blocks.teblocks.struct.TileEntityVacuumFreezer;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerVacuumFreezer extends ContainerMachineBase<TileEntityVacuumFreezer> {

    public ContainerVacuumFreezer(EntityPlayer player, TileEntityVacuumFreezer base) {
        super(player, base, 166);
        addSlotToContainer(new SlotInvSlot(base.inputSlot, 0, 34, 25));
        addSlotToContainer(new SlotInvSlot(base.outputSlot, 0, 86, 25));
    }
}
