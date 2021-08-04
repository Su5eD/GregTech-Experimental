package mods.gregtechmod.inventory.invslot;

import ic2.core.block.IInventorySlotHolder;
import ic2.core.block.invslot.InvSlot;
import net.minecraft.item.ItemStack;

import java.util.function.Predicate;

public class GtSlotFiltered extends InvSlot {
    private final Predicate<ItemStack> filter;

    public GtSlotFiltered(IInventorySlotHolder<?> base, String name, Access access, int count, Predicate<ItemStack> filter) {
        super(base, name, access, count);
        this.filter = filter;
    }

    @Override
    public boolean accepts(ItemStack stack) {
        return this.filter.test(stack);
    }
}
