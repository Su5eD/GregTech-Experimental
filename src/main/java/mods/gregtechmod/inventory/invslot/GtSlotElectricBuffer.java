package mods.gregtechmod.inventory.invslot;

import ic2.core.block.invslot.InvSlot;
import mods.gregtechmod.objects.blocks.teblocks.inv.TileEntityElectricBuffer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

public class GtSlotElectricBuffer extends InvSlot {

    public GtSlotElectricBuffer(TileEntityElectricBuffer base, String name, Access access, int count) {
        super(base, name, access, count);
    }

    @Override
    public boolean accepts(ItemStack stack) {
        return this.access.isInput();
    }

    @Override
    public void onChanged() {
        ((TileEntityElectricBuffer) this.base).inventoryModified.reset();
    }
    
    public boolean canAdd(ItemStack stack) {
        ItemStack existing = get();
        return !stack.isEmpty() && (existing.isEmpty() || ItemHandlerHelper.canItemStacksStack(existing, stack) && hasSpaceFor(existing, stack));
    }
    
    public void add(ItemStack stack) {
        if (isEmpty()) put(stack);
        else {
            ItemStack existing = get();
            if (hasSpaceFor(existing, stack)) existing.grow(stack.getCount());
        }
    }
    
    private boolean hasSpaceFor(ItemStack existing, ItemStack stack) {
        return stack.getCount() <= existing.getMaxStackSize() - existing.getCount();
    }
}
