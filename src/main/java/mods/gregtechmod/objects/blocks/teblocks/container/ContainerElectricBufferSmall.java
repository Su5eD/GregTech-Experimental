package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.slot.SlotInvSlot;
import mods.gregtechmod.objects.blocks.teblocks.inv.TileEntityElectricBufferSmall;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerElectricBufferSmall extends ContainerBase<TileEntityElectricBufferSmall> {
    
    public ContainerElectricBufferSmall(EntityPlayer player, TileEntityElectricBufferSmall base) {
        super(player, base);
        
        addSlotToContainer(new SlotInvSlot(base.buffer, 0, 80, 23));
    }

    @Override
    protected void getNetworkedFields(List<? super String> list) {
        super.getNetworkedFields(list);
        list.add("outputEnergy");
        list.add("redstoneIfFull");
        list.add("invertRedstone");
    }
}
