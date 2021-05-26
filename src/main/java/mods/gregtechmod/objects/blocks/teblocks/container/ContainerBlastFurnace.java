package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.ContainerFullInv;
import ic2.core.slot.SlotInvSlot;
import mods.gregtechmod.objects.blocks.teblocks.struct.TileEntityIndustrialBlastFurnace;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerBlastFurnace extends ContainerFullInv<TileEntityIndustrialBlastFurnace> {

    public ContainerBlastFurnace(EntityPlayer player, TileEntityIndustrialBlastFurnace base) {
        super(player, base, 166);
        addSlotToContainer(new SlotInvSlot(base.inputSlot, 0, 34, 16));
        addSlotToContainer(new SlotInvSlot(base.secondaryInput, 0, 34, 34));
        addSlotToContainer(new SlotInvSlot(base.outputSlot, 0, 86, 25));
        addSlotToContainer(new SlotInvSlot(base.outputSlot, 1, 104, 25));
    }
    
    @Override
    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("guiProgress");
        return ret;
    }
}
