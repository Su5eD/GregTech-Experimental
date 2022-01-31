package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.slot.SlotInvSlot;
import mods.gregtechmod.objects.blocks.teblocks.TileEntityMatterFabricator;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerMatterFabricator extends ContainerGtInventory<TileEntityMatterFabricator> {

    public ContainerMatterFabricator(EntityPlayer player, TileEntityMatterFabricator base) {
        super(player, base);
        
        addSlotsToContainer(2, 4, 8, 14, base.amplifierSlot);
        
        addSlotToContainer(new SlotInvSlot(base.output, 0, 128, 14));
    }

    @Override
    public void getNetworkedFields(List<? super String> list) {
        super.getNetworkedFields(list);
        list.add("progress");
    }
}
