package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.IC2;
import mods.gregtechmod.inventory.SlotInteractive;
import mods.gregtechmod.objects.blocks.teblocks.computercube.TileEntityComputerCube;
import net.minecraft.entity.player.EntityPlayer;

public abstract class ContainerComputerCube extends ContainerGtBase<TileEntityComputerCube> {

    public ContainerComputerCube(EntityPlayer player, TileEntityComputerCube base, int switchSlotX) {
        super(base);
        
        addSlotToContainer(new SlotInteractive(switchSlotX, 4, () -> {
            base.switchModule();
            IC2.platform.launchGui(player, base);
        }));
    }
}
