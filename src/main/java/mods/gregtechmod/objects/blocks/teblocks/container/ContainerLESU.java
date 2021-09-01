package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.ContainerFullInv;
import ic2.core.slot.SlotInvSlot;
import mods.gregtechmod.inventory.SlotArmor;
import mods.gregtechmod.objects.blocks.teblocks.TileEntityLESU;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;

public class ContainerLESU extends ContainerFullInv<TileEntityLESU> {

    public ContainerLESU(EntityPlayer player, TileEntityLESU base) {
        super(player, base, 166);
        
        addSlotToContainer(new SlotInvSlot(base.chargeSlot, 0, 128, 14));
        addSlotToContainer(new SlotInvSlot(base.dischargeSlot, 0, 128, 50));
        
        
        addSlotToContainer(new SlotArmor(player.inventory, 39, 152, 5, EntityEquipmentSlot.HEAD));
        addSlotToContainer(new SlotArmor(player.inventory, 38, 152, 23, EntityEquipmentSlot.CHEST));
        addSlotToContainer(new SlotArmor(player.inventory, 37, 152, 41, EntityEquipmentSlot.LEGS));
        addSlotToContainer(new SlotArmor(player.inventory, 36, 152, 59, EntityEquipmentSlot.FEET));
    }
}
