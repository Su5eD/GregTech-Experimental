package mods.gregtechmod.objects.blocks.teblocks.container;

import mods.gregtechmod.inventory.SlotInteractive;
import mods.gregtechmod.inventory.SlotInvSlotHolo;
import mods.gregtechmod.objects.blocks.teblocks.inv.TileEntityElectricTranslocator;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerElectricTranslocator extends ContainerGtInventory<TileEntityElectricTranslocator> {

    public ContainerElectricTranslocator(EntityPlayer player, TileEntityElectricTranslocator base) {
        super(player, base);
        
        addSlotToContainer(new SlotInteractive(8, 63, base::switchOutputEnergy));
        addSlotToContainer(new SlotInteractive(26, 63, base::switchInvertFilter));
        
        addSlotsToContainer(3, 3, 63, 6, 17, (index, x, y) -> new SlotInvSlotHolo(base.filter, index, x, y));
    }
    
    @Override
    public void getNetworkedFields(List<? super String> list) {
        super.getNetworkedFields(list);
        list.add("outputEnergy");
        list.add("invertFilter");
    }
}
