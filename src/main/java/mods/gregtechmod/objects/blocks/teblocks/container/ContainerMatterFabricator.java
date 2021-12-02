package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.slot.SlotInvSlot;
import mods.gregtechmod.objects.blocks.teblocks.TileEntityMatterFabricator;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerMatterFabricator extends ContainerGtBase<TileEntityMatterFabricator> {

    public ContainerMatterFabricator(EntityPlayer player, TileEntityMatterFabricator base) {
        super(player, base);

        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 4; x++) {
                int index = x + y * 4;
                
                addSlotToContainer(new SlotInvSlot(base.amplifierSlot, index, 8 + x * 18, 14 + y * 18));
            }
        }
        
        addSlotToContainer(new SlotInvSlot(base.output, 0, 128, 14));
    }

    @Override
    public void getNetworkedFields(List<? super String> list) {
        super.getNetworkedFields(list);
        list.add("progress");
    }
}
