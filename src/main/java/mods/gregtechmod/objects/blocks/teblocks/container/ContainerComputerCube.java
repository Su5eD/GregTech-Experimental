package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.IC2;
import mods.gregtechmod.inventory.SlotInteractive;
import mods.gregtechmod.objects.blocks.teblocks.computercube.TileEntityComputerCube;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerComputerCube extends ContainerGtBase<TileEntityComputerCube> {

    public ContainerComputerCube(EntityPlayer player, TileEntityComputerCube base, int switchSlotX) {
        super(base);
        
        addSlotToContainer(SlotInteractive.bothSides(switchSlotX, 4, () -> {
            base.switchModule();
            IC2.platform.launchGui(player, base);
        }));
    }

    @Override
    public void getNetworkedFields(List<? super String> list) {
        super.getNetworkedFields(list);
        list.add("module");
    }
}
