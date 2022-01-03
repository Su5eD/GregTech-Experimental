package mods.gregtechmod.inventory.invslot;

import mods.gregtechmod.objects.blocks.teblocks.inv.TileEntityElectricBufferSingle;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

public class GtSlotElectricBuffer extends GtSlot {

    public GtSlotElectricBuffer(TileEntityElectricBufferSingle base, String name, Access access, int count) {
        super(base, name, access, count);
    }

    @Override
    public void onChanged() {
        ((TileEntityElectricBufferSingle) this.base).inventoryModified.reset();
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
