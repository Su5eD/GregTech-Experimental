package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.slot.SlotInvSlot;
import mods.gregtechmod.objects.blocks.teblocks.energy.TileEntityChargerBase;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerEnergyStorage<T extends TileEntityChargerBase> extends ContainerGtInventory<T> {

    public ContainerEnergyStorage(EntityPlayer player, T base) {
        super(player, base);
        
        addSlotToContainer(new SlotInvSlot(base.chargeSlot, 0, 128, 14));
        addSlotToContainer(new SlotInvSlot(base.dischargeSlot, 0, 128, 50));
        
        addArmorSlots(player, 152, 5);
    }
}
