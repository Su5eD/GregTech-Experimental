package mods.gregtechmod.objects.blocks.tileentities.teblocks.container;

import ic2.core.ContainerFullInv;
import ic2.core.slot.SlotInvSlot;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.TileEntityIndustrialCentrifugeBase;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.TileEntityIndustrialElectrolyzer;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerIndustrialElectrolyzer extends ContainerFullInv<TileEntityIndustrialCentrifugeBase> {

    public ContainerIndustrialElectrolyzer(EntityPlayer player, TileEntityIndustrialElectrolyzer base) {
        super(player, base, 166);

        addSlotToContainer(new SlotInvSlot(base.cellSlot, 0, 50, 46));
        addSlotToContainer(new SlotInvSlot(base.inputSlot, 0, 80, 46));
        addSlotToContainer(new SlotInvSlot(base.outputSlot, 0, 50, 16));
        addSlotToContainer(new SlotInvSlot(base.outputSlot, 1, 70, 16));
        addSlotToContainer(new SlotInvSlot(base.outputSlot, 2, 90, 16));
        addSlotToContainer(new SlotInvSlot(base.outputSlot, 3, 110, 16));
    }

    @Override
    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("guiProgress");
        ret.add("tank");
        return ret;
    }
}
