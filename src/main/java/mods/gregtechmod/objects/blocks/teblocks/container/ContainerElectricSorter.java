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
        
        addInvSlotToContainer(3, 3, 63, 6, 17, base.filter, SlotInvSlotHolo::new);
        
        addSlotToContainer(new SlotInteractive(base, 134, 63, clickType -> base.switchTargetFacing()));
    }
}
