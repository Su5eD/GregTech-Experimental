package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.slot.SlotInvSlot;
import mods.gregtechmod.inventory.SlotInteractive;
import mods.gregtechmod.inventory.SlotInvSlotHolo;
import mods.gregtechmod.objects.blocks.teblocks.inv.TileEntityElectricSorter;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerElectricSorter extends ContainerElectricBuffer<TileEntityElectricSorter> {
    
    public ContainerElectricSorter(EntityPlayer player, TileEntityElectricSorter base) {
        super(player, base);
        
        addSlotToContainer(new SlotInvSlot(base.buffer, 0, 25, 23));
        
        addSlotsToContainer(3, 3, 63, 6, 17, (index, x, y) -> new SlotInvSlotHolo(base.filter, index, x, y));
        
        addSlotToContainer(SlotInteractive.serverOnly(134, 63, base::switchTargetFacing));
    }
}
