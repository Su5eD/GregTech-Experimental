package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.slot.SlotInvSlot;
import mods.gregtechmod.inventory.SlotInteractive;
import mods.gregtechmod.objects.blocks.teblocks.computercube.ComputerCubeGuide;
import mods.gregtechmod.objects.blocks.teblocks.computercube.TileEntityComputerCube;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ContainerComputerCubeGuide extends ContainerComputerCube {
    public final List<Slot> displaySlots;

    public ContainerComputerCubeGuide(EntityPlayer player, TileEntityComputerCube base) {
        super(player, base, 206);
        
        ComputerCubeGuide module = (ComputerCubeGuide) base.getActiveModule();
        
        addSlotToContainer(new SlotInteractive(190, 146, () -> {
            module.previousPage();
            displayStacks();
        }));
        addSlotToContainer(new SlotInteractive(206, 146, () -> {
            module.nextPage();
            displayStacks();
        }));

        this.displaySlots = IntStream.range(0, 5)
                .mapToObj(i -> new SlotInvSlot(module.displayStacks, i, 206, 38 + 18 * i))
                .peek(this::addSlotToContainer)
                .collect(Collectors.toList());
        
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
