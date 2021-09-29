package mods.gregtechmod.objects.blocks.teblocks.container;

import mods.gregtechmod.inventory.GtSlotInvSlot;
import mods.gregtechmod.objects.blocks.teblocks.computercube.ComputerCubeGuide;
import mods.gregtechmod.objects.blocks.teblocks.computercube.TileEntityComputerCube;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ContainerComputerCubeGuide extends ContainerComputerCube {
    public final List<Slot> displaySlots;

    public ContainerComputerCubeGuide(TileEntityComputerCube base) {
        super(base, 0, 206);
        
        ComputerCubeGuide module = (ComputerCubeGuide) base.getActiveModule();
        
        addSlotToContainer(new Slot(base, 0, 190, 146));
        addSlotToContainer(new Slot(base, 0, 206, 146));

        this.displaySlots = IntStream.range(0, 5)
                .mapToObj(i -> new GtSlotInvSlot(base, module.displayStacks, i, 206, 38 + 18 * i))
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

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickType, EntityPlayer player) {
        ComputerCubeGuide module = (ComputerCubeGuide) this.base.getActiveModule();
        
        if (slotId == 1) {
            module.previousPage();
            displayStacks();
        }
        else if (slotId == 2) {
            module.nextPage();
            displayStacks();
        }
        
        return super.slotClick(slotId, dragType, clickType, player);
    }
}
