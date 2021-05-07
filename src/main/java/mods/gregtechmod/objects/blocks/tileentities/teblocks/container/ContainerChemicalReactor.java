package mods.gregtechmod.objects.blocks.tileentities.teblocks.container;

import ic2.core.ContainerFullInv;
import ic2.core.slot.SlotInvSlot;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.TileEntityChemicalReactor;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerChemicalReactor extends ContainerFullInv<TileEntityChemicalReactor> {

    public ContainerChemicalReactor(EntityPlayer player, TileEntityChemicalReactor base) {
        super(player, base, 166);
        addSlotToContainer(new SlotInvSlot(base.inputSlot, 0, 70, 16));
        addSlotToContainer(new SlotInvSlot(base.secondaryInputSlot, 0, 90, 16));
        addSlotToContainer(new SlotInvSlot(base.outputSlot, 0, 80, 46));
    }
    
    @Override
    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("guiProgress");
        return ret;
    }
}
