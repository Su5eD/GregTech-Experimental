package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.ContainerFullInv;
import ic2.core.slot.SlotInvSlot;
import mods.gregtechmod.objects.blocks.teblocks.struct.TileEntityVacuumFreezer;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerVacuumFreezer extends ContainerFullInv<TileEntityVacuumFreezer> {

    public ContainerVacuumFreezer(EntityPlayer player, TileEntityVacuumFreezer base) {
        super(player, base, 166);
        addSlotToContainer(new SlotInvSlot(base.inputSlot, 0, 34, 25));
        addSlotToContainer(new SlotInvSlot(base.outputSlot, 0, 86, 25));
    }
    
    @Override
    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("guiProgress");
        return ret;
    }
}
