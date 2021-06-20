package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.slot.SlotInvSlot;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityFluidGenerator;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerPlasmaGenerator extends ContainerFluidGenerator {

    public ContainerPlasmaGenerator(EntityPlayer player, TileEntityFluidGenerator base) {
        super(player, base);
    }

    @Override
    protected void initSlots() {
        addSlotToContainer(new SlotInvSlot(getInputSlot(), 0, 85, 17));
        addSlotToContainer(new SlotInvSlot(getOutputSlot(), 0, 85, 53));
    }
}
