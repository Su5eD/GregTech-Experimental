package mods.gregtechmod.objects.blocks.machines.container;

import ic2.core.ContainerFullInv;
import ic2.core.slot.SlotInvSlot;
import mods.gregtechmod.inventory.SlotUpgradeInvSlot;
import mods.gregtechmod.objects.blocks.machines.tileentity.TileEntityGtCentrifuge;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerGtCentrifuge extends ContainerFullInv<TileEntityGtCentrifuge> {

    public ContainerGtCentrifuge(EntityPlayer player, TileEntityGtCentrifuge base) {
        super(player, base, 166);
        addSlotToContainer(new SlotInvSlot(base.cellSlot, 0, 50, 5));
        addSlotToContainer(new SlotInvSlot(base.inputSlot, 0, 80, 35));
        addSlotToContainer(new SlotInvSlot(base.outputSlot, 0, 80, 5));
        addSlotToContainer(new SlotInvSlot(base.outputSlot, 1, 110, 35));
        addSlotToContainer(new SlotInvSlot(base.outputSlot, 2, 80, 65));
        addSlotToContainer(new SlotInvSlot(base.outputSlot, 3, 50, 35));
        //Hidden slots
        addSlotToContainer(new SlotUpgradeInvSlot(base.upgradeSlot, 0, 0, 0));
        addSlotToContainer(new SlotUpgradeInvSlot(base.upgradeSlot, 1, 0, 16));
        addSlotToContainer(new SlotUpgradeInvSlot(base.upgradeSlot, 2, 0, 32));
        addSlotToContainer(new SlotUpgradeInvSlot(base.upgradeSlot, 3, 0, 48));
    }


    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("guiProgress");
        ret.add("lavaTank");
        return ret;
    }
}
