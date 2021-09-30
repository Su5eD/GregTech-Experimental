package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.slot.SlotInvSlot;
import mods.gregtechmod.objects.blocks.teblocks.computercube.ComputerCubeScanner;
import mods.gregtechmod.objects.blocks.teblocks.computercube.TileEntityComputerCube;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerComputerCubeScanner extends ContainerComputerCubeMain {

    public ContainerComputerCubeScanner(EntityPlayer player, TileEntityComputerCube base) {
        super(player, base);
        
        ComputerCubeScanner module = (ComputerCubeScanner) base.getActiveModule();
        
        addSlotToContainer(new SlotInvSlot(module.inputSlot, 0, 8, 28));
        addSlotToContainer(new SlotInvSlot(module.inputSlot, 1, 26, 28));
        
        addSlotToContainer(new SlotInvSlot(module.outputSlot, 0, 134, 28));
        addSlotToContainer(new SlotInvSlot(module.outputSlot, 1, 152, 28));
    }
}
