package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.slot.SlotInvSlot;
import mods.gregtechmod.objects.blocks.teblocks.energy.TileEntityChargeOMat;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerChargeOMat extends ContainerGtInventory<TileEntityChargeOMat> {

    public ContainerChargeOMat(EntityPlayer player, TileEntityChargeOMat base) {
        super(player, base);

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                int index = x + y * 3;
                int yPos = 5 + 18 * y;

                int chargeX = 8 + 18 * x;
                addSlotToContainer(new SlotInvSlot(base.chargeSlots[index], 0, chargeX, yPos));

                int dischargeX = 97 + 18 * x;
                addSlotToContainer(new SlotInvSlot(base.outputSlot, index, dischargeX, yPos));
            }
        }

        addArmorSlots(player, 152, 5);
    }
}
