package mods.gregtechmod.objects.blocks.tileentities.teblocks.container;

import ic2.core.ContainerFullInv;
import ic2.core.slot.SlotInvSlot;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.TileEntityIndustrialCentrifuge;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerIndustrialCentrifuge extends ContainerFullInv<TileEntityIndustrialCentrifuge> {

    public ContainerIndustrialCentrifuge(EntityPlayer player, TileEntityIndustrialCentrifuge base) {
        super(player, base, 166);
        addSlotToContainer(new SlotInvSlot(base.cellSlot, 0, 50, 5));
        addSlotToContainer(new SlotInvSlot(base.inputSlot, 0, 80, 35));
        addSlotToContainer(new SlotInvSlot(base.outputSlot, 0, 80, 5));
        addSlotToContainer(new SlotInvSlot(base.outputSlot, 1, 110, 35));
        addSlotToContainer(new SlotInvSlot(base.outputSlot, 2, 80, 65));
        addSlotToContainer(new SlotInvSlot(base.outputSlot, 3, 50, 35));
    }

    @Override
    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("guiProgress");
        ret.add("tank");
        return ret;
    }
}
