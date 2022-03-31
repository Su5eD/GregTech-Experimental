package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.slot.SlotInvSlot;
import mods.gregtechmod.inventory.SlotInteractive;
import mods.gregtechmod.objects.blocks.teblocks.inv.TileEntityElectricBufferAdvanced;
import mods.gregtechmod.util.ButtonClick;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerElectricBufferAdvanced extends ContainerElectricBuffer<TileEntityElectricBufferAdvanced> {

    public ContainerElectricBufferAdvanced(EntityPlayer player, TileEntityElectricBufferAdvanced base) {
        super(player, base);

        addSlotToContainer(new SlotInvSlot(base.buffer, 0, 80, 23));

        addSlotToContainer(SlotInteractive.serverOnly(80, 63, click -> base.targetSlot = Math.max(0, base.targetSlot - (click == ButtonClick.SHIFT_MOVE ? 16 : 1))));
        addSlotToContainer(SlotInteractive.serverOnly(134, 63, click -> base.targetSlot = Math.min(8192, base.targetSlot + (click == ButtonClick.SHIFT_MOVE ? 16 : 1))));
    }

    @Override
    public void getNetworkedFields(List<? super String> list) {
        super.getNetworkedFields(list);
        list.add("targetSlot");
    }
}
