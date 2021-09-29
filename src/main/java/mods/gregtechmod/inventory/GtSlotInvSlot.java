package mods.gregtechmod.inventory;

import ic2.core.block.invslot.InvSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class GtSlotInvSlot extends Slot {
    private final InvSlot invSlot;
    private final int index;
    
    public GtSlotInvSlot(IInventory inventory, InvSlot invSlot, int index, int xPosition, int yPosition) {
        this(inventory, invSlot, index, index, xPosition, yPosition);
    }

    public GtSlotInvSlot(IInventory inventory, InvSlot invSlot, int index, int slotIndex, int xPosition, int yPosition) {
        super(inventory, slotIndex, xPosition, yPosition);

        this.invSlot = invSlot;
        this.index = index;
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
}
