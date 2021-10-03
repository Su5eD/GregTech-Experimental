package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.slot.SlotInvSlot;
import mods.gregtechmod.objects.blocks.teblocks.TileEntityAdvancedPump;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerAdvancedPump extends ContainerGtBase<TileEntityAdvancedPump> {

    public ContainerAdvancedPump(EntityPlayer player, TileEntityAdvancedPump base) {
        super(player, base);
        
        addSlotToContainer(new SlotInvSlot(base.inputSlot, 0, 80, 17));
        addSlotToContainer(new SlotInvSlot(base.outputSlot, 0, 80, 53));
        addSlotToContainer(new SlotInvSlot(base.pipeSlot, 0, 116, 17));
    }
}
