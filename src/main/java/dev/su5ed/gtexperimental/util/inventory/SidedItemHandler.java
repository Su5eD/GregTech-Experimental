package dev.su5ed.gtexperimental.util.inventory;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface SidedItemHandler {
    int getSlots(@Nullable Direction side);

    ItemStack getStackInSlot(int slot, @Nullable Direction side);

    ItemStack insertItem(int slot, ItemStack stack, @Nullable Direction side, boolean simulate);
    
    ItemStack extractItem(int slot, int amount, @Nullable Direction side, boolean simulate);
    
    int getSlotLimit(int slot, @Nullable Direction side);
    
    boolean isItemValid(int slot, ItemStack stack, @Nullable Direction side);
    
    void setStackInSlot(int slot, ItemStack stack, @Nullable Direction side);
}
