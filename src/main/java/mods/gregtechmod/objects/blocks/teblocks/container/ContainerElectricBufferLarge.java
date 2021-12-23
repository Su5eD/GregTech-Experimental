package mods.gregtechmod.objects.blocks.teblocks.container;

import mods.gregtechmod.objects.blocks.teblocks.inv.TileEntityElectricBufferLarge;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerElectricBufferLarge extends ContainerElectricBuffer<TileEntityElectricBufferLarge> {
    
    public ContainerElectricBufferLarge(EntityPlayer player, TileEntityElectricBufferLarge base) {
        super(player, base);
        
        addInvSlotToContainer(3, 9, 8, 5, base.buffer);
    }
}
