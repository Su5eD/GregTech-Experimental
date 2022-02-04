package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.slot.SlotInvSlot;
import mods.gregtechmod.inventory.SlotInteractive;
import mods.gregtechmod.objects.blocks.teblocks.inv.TileEntityElectricTypeSorter;
import mods.gregtechmod.util.ButtonClick;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerElectricTypeSorter extends ContainerElectricBuffer<TileEntityElectricTypeSorter> {

    public ContainerElectricTypeSorter(EntityPlayer player, TileEntityElectricTypeSorter base) {
        super(player, base);

        addSlotToContainer(new SlotInvSlot(base.buffer, 0, 25, 23));

        addSlotToContainer(SlotInteractive.serverOnly(70, 22, click -> {
            if (click == ButtonClick.MOUSE_RIGHT) base.previousType();
            else base.nextType();
        }));
        addSlotToContainer(SlotInteractive.serverOnly(134, 63, base::switchTargetFacing));
    }

    @Override
    public void getNetworkedFields(List<? super String> list) {
        super.getNetworkedFields(list);
        list.add("type");
    }
}
