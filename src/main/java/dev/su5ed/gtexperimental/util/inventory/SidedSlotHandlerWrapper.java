package dev.su5ed.gtexperimental.util.inventory;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.Nullable;

public class SidedSlotHandlerWrapper implements IItemHandlerModifiable {
    private final SidedItemHandler wrapped;
    @Nullable
    private final Direction side;

    public SidedSlotHandlerWrapper(SidedItemHandler wrapped, @Nullable Direction side) {
        this.wrapped = wrapped;
        this.side = side;
    }

    @Override
    public int getSlots() {
        return this.wrapped.getSlots(this.side);
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return this.wrapped.getStackInSlot(slot, this.side);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        return this.wrapped.insertItem(slot, stack, this.side, simulate);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return this.wrapped.extractItem(slot, amount, this.side, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return this.wrapped.getSlotLimit(slot, this.side);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return this.wrapped.isItemValid(slot, stack, this.side);
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        this.wrapped.setStackInSlot(slot, stack, this.side);
    }
}
