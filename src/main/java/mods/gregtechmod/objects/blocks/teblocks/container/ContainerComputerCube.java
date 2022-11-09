package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.IC2;
import ic2.core.WorldData;
import mods.gregtechmod.inventory.SlotInteractive;
import mods.gregtechmod.objects.blocks.teblocks.computercube.TileEntityComputerCube;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerComputerCube extends ContainerGtBase<TileEntityComputerCube> {

    public ContainerComputerCube(EntityPlayer player, TileEntityComputerCube base, int switchSlotX) {
        super(base);

        addSlotToContainer(SlotInteractive.serverOnly(switchSlotX, 4, () -> {
            base.switchModule();
            // Request immediate data update
            IC2.network.get(false).onTickEnd(WorldData.get(base.getWorld()));
            IC2.platform.launchGui(player, base);
        }));
    }

    @Override
    public void getNetworkedFields(List<? super String> list) {
        super.getNetworkedFields(list);
        list.add("module");
    }
}
