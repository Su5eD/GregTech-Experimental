package mods.gregtechmod.objects.blocks.teblocks.container;

import mods.gregtechmod.inventory.SlotInteractive;
import mods.gregtechmod.objects.blocks.teblocks.inv.TileEntityElectricTranslocatorAdvanced;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerElectricTranslocatorAdvanced extends ContainerElectricTranslocator<TileEntityElectricTranslocatorAdvanced> {

    public ContainerElectricTranslocatorAdvanced(EntityPlayer player, TileEntityElectricTranslocatorAdvanced base) {
        super(player, base);
        
        addSlotToContainer(SlotInteractive.serverOnly(43, 5, base::switchInputFacing));
        addSlotToContainer(SlotInteractive.serverOnly(117, 5, base::switchOutputFacing));
    }

    @Override
    public void getNetworkedFields(List<? super String> list) {
        super.getNetworkedFields(list);
        list.add("inputSide");
        list.add("outputSide");
    }
}
