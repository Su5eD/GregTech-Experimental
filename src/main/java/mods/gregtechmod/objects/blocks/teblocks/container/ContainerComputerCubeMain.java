package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.IC2;
import mods.gregtechmod.inventory.SlotInteractive;
import mods.gregtechmod.objects.blocks.teblocks.computercube.TileEntityComputerCube;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerComputerCubeMain extends ContainerGtInventory<TileEntityComputerCube> {
    
    public ContainerComputerCubeMain(EntityPlayer player, TileEntityComputerCube base) {
        super(player, base);
        
        addSlotToContainer(new SlotInteractive(156, 4, () -> {
            base.switchModule();
            IC2.platform.launchGui(player, base);
        }));
    }
}
