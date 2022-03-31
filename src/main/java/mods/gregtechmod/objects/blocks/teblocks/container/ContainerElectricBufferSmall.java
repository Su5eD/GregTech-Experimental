package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.slot.SlotInvSlot;
import mods.gregtechmod.objects.blocks.teblocks.inv.TileEntityElectricBufferSingle;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerElectricBufferSmall<T extends TileEntityElectricBufferSingle> extends ContainerElectricBuffer<T> {

    public ContainerElectricBufferSmall(EntityPlayer player, T base) {
        super(player, base);

        addSlotToContainer(new SlotInvSlot(base.buffer, 0, 80, 23));
    }
}
