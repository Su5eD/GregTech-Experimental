package mods.gregtechmod.objects.blocks.teblocks.container;

import mods.gregtechmod.inventory.SlotHoloItem;
import mods.gregtechmod.inventory.SlotInteractive;
import mods.gregtechmod.objects.blocks.teblocks.computercube.ComputerCubeGuide;
import mods.gregtechmod.objects.blocks.teblocks.computercube.TileEntityComputerCube;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import one.util.streamex.IntStreamEx;

public class ContainerComputerCubeGuide extends ContainerComputerCube {

    public ContainerComputerCubeGuide(EntityPlayer player, TileEntityComputerCube base) {
        super(player, base, 206);

        IntStreamEx.range(5)
            .mapToObj(i -> new SlotHoloItem(() -> {
                ComputerCubeGuide.GuidePage page = ((ComputerCubeGuide) this.base.getActiveModule()).getCurrentPage();
                return i >= page.stacks.size() ? ItemStack.EMPTY : page.stacks.get(i);
            }, 206, 38 + 18 * (4 - i)))
            .peek(this::addSlotToContainer)
            .toList();

        addSlotToContainer(SlotInteractive.serverOnly(190, 146, () -> {
            ((ComputerCubeGuide) base.getActiveModule()).previousPage();
            detectAndSendChanges();
        }));
        addSlotToContainer(SlotInteractive.serverOnly(206, 146, () -> {
            ((ComputerCubeGuide) base.getActiveModule()).nextPage();
            detectAndSendChanges();
        }));
    }
}
