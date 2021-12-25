package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.slot.SlotInvSlot;
import mods.gregtechmod.objects.blocks.teblocks.inv.TileEntityElectricBuffer;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerElectricBufferSmall<T extends TileEntityElectricBuffer> extends ContainerElectricBuffer<T> {
    
    public ContainerElectricBufferSmall(EntityPlayer player, T base) {
        super(player, base);
        
        addSlotToContainer(new SlotInvSlot(base.buffer, 0, 80, 23));
    }
}
