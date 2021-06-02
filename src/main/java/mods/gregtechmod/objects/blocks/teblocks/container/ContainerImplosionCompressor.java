package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.slot.SlotInvSlot;
import mods.gregtechmod.objects.blocks.teblocks.struct.TileEntityImplosionCompressor;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerImplosionCompressor extends ContainerMachineBase<TileEntityImplosionCompressor> {

    public ContainerImplosionCompressor(EntityPlayer player, TileEntityImplosionCompressor base) {
        super(player, base, 166);
        addSlotToContainer(new SlotInvSlot(base.inputSlot, 0, 34, 16));
        addSlotToContainer(new SlotInvSlot(base.secondaryInput, 0, 34, 34));
        addSlotToContainer(new SlotInvSlot(base.outputSlot, 0, 86, 25));
        addSlotToContainer(new SlotInvSlot(base.outputSlot, 1, 104, 25));
    }
}
