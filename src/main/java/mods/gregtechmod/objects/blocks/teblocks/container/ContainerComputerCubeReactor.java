package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.block.invslot.InvSlot;
import ic2.core.slot.SlotInvSlot;
import ic2.core.util.StackUtil;
import mods.gregtechmod.objects.blocks.teblocks.computercube.ComputerCubeReactor;
import mods.gregtechmod.objects.blocks.teblocks.computercube.TileEntityComputerCube;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.function.IntSupplier;

public class ContainerComputerCubeReactor extends ContainerComputerCube { // TODO Cleanup with SlotInteractive

    public ContainerComputerCubeReactor(TileEntityComputerCube base) {
        super(base, 156);
        
        ComputerCubeReactor module = (ComputerCubeReactor) base.getActiveModule();
        
        Slot slotItemSelection = new SlotStacksInvSlot(module.selection, 0, 153, 28, ComputerCubeReactor.COMPONENTS, () -> 0);
        IntSupplier offsetSupplier = () -> {
            ItemStack content = slotItemSelection.getStack();
            for (int i = 0; i < ComputerCubeReactor.COMPONENTS.size(); i++) {
                ItemStack stack = ComputerCubeReactor.COMPONENTS.get(i);
                if (StackUtil.checkItemEquality(content, stack)) return i;
            }
            return 0;
        };
        addSlotToContainer(slotItemSelection); // Item Selection
        addSlotToContainer(new Slot(base, -1, 156, 54)); // Save
        addSlotToContainer(new Slot(base, -1, 156, 70)); // Load
        addSlotToContainer(new Slot(base, -1, 156, 86)); // Start/Stop Reactor
        
        for (int y = 0; y < 6; y++) {
            for (int x = 0; x < 9; x++) {
                addSlotToContainer(new SlotStacksInvSlot(module.content, x + y * 9, 5 + x * 16, 5 + y * 16, ComputerCubeReactor.COMPONENTS, offsetSupplier)); 
            }
        }
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickType, EntityPlayer player) {
        ComputerCubeReactor module = (ComputerCubeReactor) this.base.getActiveModule();
        
        if (slotId == 2) {
            module.saveNuclearReactor();
            detectAndSendChanges();
        }
        else if (slotId == 3) {
            module.loadNuclearReactor();
            detectAndSendChanges();
        }
        else if (slotId == 4) module.switchNuclearReactor();
        
        if (slotId == 1 || slotId > 4 && slotId < 59) {
            Slot slot = getSlot(slotId);
            if (slot instanceof SlotStacksInvSlot) return ((SlotStacksInvSlot) slot).slotClick(dragType, clickType);
        }
        
        return super.slotClick(slotId, dragType, clickType, player);
    }
    
    private static class SlotStacksInvSlot extends SlotInvSlot {
        private final List<ItemStack> stacks;
        private final IntSupplier offset;
        
        public SlotStacksInvSlot(InvSlot invSlot, int index, int x, int y, List<ItemStack> stacks, IntSupplier offset) {
            super(invSlot, index, x, y);
            
            this.stacks = stacks;
            this.offset = offset;
        }
        
        public ItemStack slotClick(int dragType, ClickType clickType) {
            ItemStack content = getStack();

            if (clickType == ClickType.QUICK_MOVE) putStack(ItemStack.EMPTY);
            else if (clickType == ClickType.PICKUP && dragType < 1) {
                int offset = this.offset.getAsInt();

                if (content.isEmpty()) putStack(this.stacks.get(offset).copy());
                else {
                    for (int i = 1; i < this.stacks.size(); i++) {
                        if (StackUtil.checkItemEquality(content, this.stacks.get(i - 1))) {
                            putStack(this.stacks.get(i).copy());
                            return ItemStack.EMPTY;
                        }
                    }
                    putStack(ItemStack.EMPTY);
                }
            }
                            
            return ItemStack.EMPTY;
        }
    }
}
