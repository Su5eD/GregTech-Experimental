package mods.gregtechmod.objects.blocks.teblocks.container;

import mods.gregtechmod.inventory.SlotInteractive;
import mods.gregtechmod.objects.blocks.teblocks.inv.TileEntityElectricBuffer;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerElectricBuffer<T extends TileEntityElectricBuffer> extends ContainerGtInventory<T> {

    public ContainerElectricBuffer(EntityPlayer player, T base) {
        super(player, base);

        addSlotToContainer(SlotInteractive.serverOnly(8, 63, base::switchOutputEnergy));
        addSlotToContainer(SlotInteractive.serverOnly(26, 63, base::switchRedstoneIfFull));
        addSlotToContainer(SlotInteractive.serverOnly(44, 63, base::switchInvertRedstone));
    }

    @Override
    public void getNetworkedFields(List<? super String> list) {
        super.getNetworkedFields(list);
        list.add("outputEnergy");
        list.add("redstoneIfFull");
        list.add("invertRedstone");
    }
}
