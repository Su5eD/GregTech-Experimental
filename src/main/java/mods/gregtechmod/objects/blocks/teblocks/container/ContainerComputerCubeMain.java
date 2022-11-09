package mods.gregtechmod.objects.blocks.teblocks.container;

import mods.gregtechmod.objects.blocks.teblocks.computercube.TileEntityComputerCube;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerComputerCubeMain extends ContainerComputerCube {

    public ContainerComputerCubeMain(EntityPlayer player, TileEntityComputerCube base) {
        super(player, base, 156);

        addPlayerInventorySlots(player, 166);
    }
}
