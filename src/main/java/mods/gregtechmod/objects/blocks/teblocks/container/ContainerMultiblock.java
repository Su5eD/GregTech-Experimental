package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.ContainerFullInv;
import ic2.core.slot.SlotInvSlot;
import mods.gregtechmod.objects.blocks.teblocks.multiblock.TileEntityMultiBlockBase;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerMultiblock extends ContainerFullInv<TileEntityMultiBlockBase> {

    public ContainerMultiblock(EntityPlayer player, TileEntityMultiBlockBase base) {
        super(player, base, 166);
        addSlotToContainer(new SlotInvSlot(base.machinePartSlot, 0, 152, 5));
    }
}
