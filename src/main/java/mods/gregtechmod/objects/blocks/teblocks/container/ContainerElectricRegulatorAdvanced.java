package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.slot.SlotInvSlot;
import mods.gregtechmod.inventory.SlotInteractive;
import mods.gregtechmod.inventory.SlotInvSlotSizeFilter;
import mods.gregtechmod.objects.blocks.teblocks.inv.TileEntityElectricRegulatorAdvanced;
import mods.gregtechmod.util.ButtonClick;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerElectricRegulatorAdvanced extends ContainerGtInventory<TileEntityElectricRegulatorAdvanced> {

    public ContainerElectricRegulatorAdvanced(EntityPlayer player, TileEntityElectricRegulatorAdvanced base) {
        super(player, base);

        addSlotsToContainer(3, 3, 8, 6, 18, (index, x, y) -> new SlotInvSlot(base.bufferSlots.get(index), 0, x, y));
        addSlotsToContainer(3, 3, 64, 7, 17, (index, x, y) -> new SlotInvSlotSizeFilter(base.filter, index, x, y));
        addSlotsToContainer(3, 3, 119, 7, 17, (index, x, y) -> SlotInteractive.serverOnly(x, y, click -> {
            if (click == ButtonClick.MOUSE_RIGHT) base.slotIndices[index]++;
            else if (click == ButtonClick.MOUSE_LEFT) base.slotIndices[index] = Math.max(0, base.slotIndices[index] - 1);
        }));

        addSlotToContainer(SlotInteractive.serverOnly(8, 63, base::switchOutputEnergy));
    }

    @Override
    public void getNetworkedFields(List<? super String> list) {
        super.getNetworkedFields(list);
        list.add("outputEnergy");
        list.add("slotIndices");
    }
}
