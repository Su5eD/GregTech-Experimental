package mods.gregtechmod.objects.blocks.teblocks.container;

import mods.gregtechmod.inventory.SlotInvSlotHolo;
import mods.gregtechmod.objects.blocks.teblocks.TileEntityAdvancedSafe;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerAdvancedSafe extends ContainerGtInventory<TileEntityAdvancedSafe> {

    public ContainerAdvancedSafe(EntityPlayer player, TileEntityAdvancedSafe base) {
        super(player, base);
        
        addSlotsToContainer(3, 9, 8, 23, base.content);
        
        addSlotToContainer(new SlotInvSlotHolo(base.filter, 0, 80, 5));
    }
}
