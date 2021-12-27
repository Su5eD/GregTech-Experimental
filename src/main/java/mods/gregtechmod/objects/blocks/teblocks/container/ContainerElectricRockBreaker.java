package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.slot.SlotInvSlot;
import mods.gregtechmod.objects.blocks.teblocks.inv.TileEntityElectricRockBreaker;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerElectricRockBreaker extends ContainerElectricBufferSmall<TileEntityElectricRockBreaker> {
    
    public ContainerElectricRockBreaker(EntityPlayer player, TileEntityElectricRockBreaker base) {
        super(player, base);
        
        addSlotToContainer(new SlotInvSlot(base.redstoneSlot, 0, 80, 41));
    }
}
