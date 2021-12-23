package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.slot.SlotInvSlot;
import mods.gregtechmod.objects.blocks.teblocks.inv.TileEntityElectricBufferSmall;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerElectricBufferSmall extends ContainerElectricBuffer<TileEntityElectricBufferSmall> {
    
    public ContainerElectricBufferSmall(EntityPlayer player, TileEntityElectricBufferSmall base) {
        super(player, base);
        
        addSlotToContainer(new SlotInvSlot(base.buffer, 0, 80, 23));
    }
}
