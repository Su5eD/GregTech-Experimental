package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.slot.SlotInvSlot;
import mods.gregtechmod.objects.blocks.teblocks.TileEntityChemicalReactor;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerChemicalReactor extends ContainerMachineBase<TileEntityChemicalReactor> {

    public ContainerChemicalReactor(EntityPlayer player, TileEntityChemicalReactor base) {
        super(player, base, 166);
        addSlotToContainer(new SlotInvSlot(base.inputSlot, 0, 70, 16));
        addSlotToContainer(new SlotInvSlot(base.secondaryInputSlot, 0, 90, 16));
        addSlotToContainer(new SlotInvSlot(base.outputSlot, 0, 80, 46));
    }
}
