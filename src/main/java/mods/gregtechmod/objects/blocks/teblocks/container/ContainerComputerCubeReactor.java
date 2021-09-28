package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.block.invslot.InvSlot;
import ic2.core.util.StackUtil;
import mods.gregtechmod.objects.blocks.teblocks.computercube.ComputerCubeReactor;
import mods.gregtechmod.objects.blocks.teblocks.computercube.TileEntityComputerCube;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.function.IntSupplier;

public class ContainerComputerCubeReactor extends ContainerComputerCube {

    public ContainerComputerCubeReactor(TileEntityComputerCube base) {
        super(base, 0);
        
        ComputerCubeReactor module = (ComputerCubeReactor) base.getActiveModule();
        
        Slot slotItemSelection = new SlotStacksInvSlot(base, module.selection, 0, 56, 153, 28, ComputerCubeReactor.COMPONENTS, () -> 0);
        IntSupplier offsetSupplier = () -> {
            ItemStack content = slotItemSelection.getStack();
            for (int i = 0; i < ComputerCubeReactor.COMPONENTS.size(); i++) {
                ItemStack stack = ComputerCubeReactor.COMPONENTS.get(i);
                if (StackUtil.checkItemEquality(content, stack)) return i;
            }
            return 0;
        };
        addSlotToContainer(slotItemSelection); // Item Selection
        addSlotToContainer(new Slot(base, 1, 156, 54)); // Save
        addSlotToContainer(new Slot(base, 2, 156, 70)); // Load
        addSlotToContainer(new Slot(base, 3, 156, 86)); // Start/Stop Reactor
        
        for (int y = 0; y < 6; y++) {
            for (int x = 0; x < 9; x++) {
                addSlotToContainer(new SlotStacksInvSlot(base, module.content, x + y * 9, 5 + x * 16, 5 + y * 16, ComputerCubeReactor.COMPONENTS, offsetSupplier)); 
            }
        }
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickType, EntityPlayer player) {
        ComputerCubeReactor module = (ComputerCubeReactor) base.getActiveModule();
        
        if (slotId == 2) {
            module.saveNuclearReactor();
            onCraftMatrixChanged(this.base);
        }
        else if (slotId == 3) {
            module.loadNuclearReactor();
            onCraftMatrixChanged(this.base);
        }
        else if (slotId == 4) module.switchNuclearReactor();
        
        if (slotId == 1 || slotId > 4 && slotId < 59) {
            Slot slot = getSlot(slotId);
            if (slot instanceof SlotStacksInvSlot) return ((SlotStacksInvSlot) slot).slotClick(dragType, clickType);
        }
        
        return super.slotClick(slotId, dragType, clickType, player);
    }
    
    private static class SlotStacksInvSlot extends Slot {
        private final InvSlot invSlot;
        private final int index;
        private final List<ItemStack> stacks;
        private final IntSupplier offset;
        
        public SlotStacksInvSlot(IInventory parent, InvSlot invSlot, int index, int x, int y, List<ItemStack> stacks, IntSupplier offset) {
            this(parent, invSlot, index, index, x, y, stacks, offset);
        }
        
        public SlotStacksInvSlot(IInventory parent, InvSlot invSlot, int index, int slotIndex, int x, int y, List<ItemStack> stacks, IntSupplier offset) {
            super(parent, slotIndex, x, y);
            
            this.invSlot = invSlot;
            this.index = index;
            this.stacks = stacks;
            this.offset = offset;
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return this.invSlot.accepts(stack);
        }

        @Override
        public ItemStack getStack() {
            return this.invSlot.get(this.index);
        }

        @Override
        public void putStack(ItemStack stack) {
            this.invSlot.put(this.index, stack);
            onSlotChanged();
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
