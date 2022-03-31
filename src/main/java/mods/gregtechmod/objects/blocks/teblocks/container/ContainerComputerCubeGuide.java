package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.slot.SlotInvSlot;
import mods.gregtechmod.inventory.SlotInteractive;
import mods.gregtechmod.objects.blocks.teblocks.computercube.ComputerCubeGuide;
import mods.gregtechmod.objects.blocks.teblocks.computercube.TileEntityComputerCube;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import one.util.streamex.IntStreamEx;

import java.util.List;

public class ContainerComputerCubeGuide extends ContainerComputerCube {
    public final List<? extends Slot> displaySlots;

    public ContainerComputerCubeGuide(EntityPlayer player, TileEntityComputerCube base) {
        super(player, base, 206);

        addSlotToContainer(SlotInteractive.serverOnly(190, 146, () -> {
            ((ComputerCubeGuide) base.getActiveModule()).previousPage();
            displayStacks();
            detectAndSendChanges();
        }));
        addSlotToContainer(SlotInteractive.serverOnly(206, 146, () -> {
            ((ComputerCubeGuide) base.getActiveModule()).nextPage();
            displayStacks();
            detectAndSendChanges();
        }));

        ComputerCubeGuide module = (ComputerCubeGuide) base.getActiveModule();
        this.displaySlots = IntStreamEx.range(0, 5)
            .mapToObj(i -> new SlotInvSlot(module.displayStacks, i, 206, 38 + 18 * i))
            .peek(this::addSlotToContainer)
            .toList();

        displayStacks();
    }

    private void displayStacks() {
        ComputerCubeGuide.GuidePage page = ((ComputerCubeGuide) this.base.getActiveModule()).getCurrentPage();
        int size = this.displaySlots.size();

        for (int i = size - 1; i >= 0; i--) {
            Slot slot = this.displaySlots.get(i);
            int j = size - 1 - i;

            if (page.stacks.size() > j) slot.putStack(page.stacks.get(j));
            else slot.putStack(ItemStack.EMPTY);

            slot.onSlotChanged();
        }
    }
}
