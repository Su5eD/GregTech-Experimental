package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.slot.SlotInvSlot;
import mods.gregtechmod.inventory.SlotInteractive;
import mods.gregtechmod.objects.blocks.teblocks.inv.TileEntityElectricBufferAdvanced;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;

import java.util.List;

public class ContainerElectricBufferAdvanced extends ContainerElectricBuffer<TileEntityElectricBufferAdvanced> {
    
    public ContainerElectricBufferAdvanced(EntityPlayer player, TileEntityElectricBufferAdvanced base) {
        super(player, base);
        
        addSlotToContainer(new SlotInvSlot(base.buffer, 0, 80, 23));
        
        addSlotToContainer(new SlotInteractive(base, 80, 63, clickType -> base.targetSlot = Math.max(0, base.targetSlot - (clickType == ClickType.QUICK_MOVE ? 16 : 1))));
        addSlotToContainer(new SlotInteractive(base, 134, 63, clickType -> base.targetSlot = Math.min(8192, base.targetSlot + (clickType == ClickType.QUICK_MOVE ? 16 : 1))));
    }

    @Override
    public void getNetworkedFields(List<? super String> list) {
        super.getNetworkedFields(list);
        list.add("targetSlot");
    }
}
