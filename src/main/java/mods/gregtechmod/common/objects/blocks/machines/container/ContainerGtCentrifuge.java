package mods.gregtechmod.common.objects.blocks.machines.container;

import ic2.core.ContainerFullInv;
import ic2.core.slot.SlotInvSlot;
import mods.gregtechmod.common.objects.blocks.machines.tileentity.TileEntityGtCentrifuge;
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
        addSlotToContainer(new SlotInvSlot(base.upgradeSlot, 0, 0, 0) {
            @Override
            public boolean isEnabled() {
                return false;
            }
        });
        addSlotToContainer(new SlotInvSlot(base.upgradeSlot, 1, 0, 16) {
            @Override
            public boolean isEnabled() {
                return false;
            }
        });
        addSlotToContainer(new SlotInvSlot(base.upgradeSlot, 2, 0, 32) {
            @Override
            public boolean isEnabled() {
                return false;
            }
        });
        addSlotToContainer(new SlotInvSlot(base.upgradeSlot, 3, 0, 48) {
            @Override
            public boolean isEnabled() {
                return false;
            }
        });
    }


    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("guiProgress");
        ret.add("lavaTank");
        return ret;
    }
}
