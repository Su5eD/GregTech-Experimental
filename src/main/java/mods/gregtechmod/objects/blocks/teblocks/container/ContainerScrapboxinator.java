package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.slot.SlotInvSlot;
import mods.gregtechmod.objects.blocks.teblocks.inv.TileEntityScrapboxinator;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerScrapboxinator extends ContainerElectricBufferSmall<TileEntityScrapboxinator> {

    public ContainerScrapboxinator(EntityPlayer player, TileEntityScrapboxinator base) {
        super(player, base);

        addSlotToContainer(new SlotInvSlot(base.scrapSlot, 0, 80, 41));
    }
}
