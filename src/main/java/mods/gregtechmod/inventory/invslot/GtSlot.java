package mods.gregtechmod.inventory.invslot;

import ic2.core.block.IInventorySlotHolder;
import ic2.core.block.invslot.InvSlot;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityAutoNBT;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import one.util.streamex.IntStreamEx;

public class GtSlot extends InvSlot {

    public GtSlot(IInventorySlotHolder<?> base, String name, Access access, int count) {
        super(base, name, access, count);
    }

    @Override
    public boolean accepts(ItemStack stack) {
        return this.access.isInput();
    }

    @Override
    public void onChanged() {
        ((TileEntityAutoNBT) this.base).onInventoryChanged();
    }

    public boolean canAdd(ItemStack stack) {
        ItemStack existing = get();
        return !stack.isEmpty() && (existing.isEmpty() || ItemHandlerHelper.canItemStacksStack(existing, stack) && GtUtil.canGrowStack(existing, stack));
    }

    public boolean add(ItemStack stack) {
        return IntStreamEx.range(0, size())
            .findFirst(i -> add(i, stack))
            .isPresent();
    }

    public boolean add(int index, ItemStack stack) {
        if (isEmpty(index)) {
            put(index, stack);
            return true;
        }
        else {
            ItemStack existing = get(index);
            if (GtUtil.canGrowStack(existing, stack)) {
                existing.grow(stack.getCount());
                return true;
            }
        }
        return false;
    }
}
