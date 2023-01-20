package dev.su5ed.gtexperimental.util.inventory;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class SlotAwareItemHandler implements IItemHandler, IItemHandlerModifiable, INBTSerializable<CompoundTag> {
    private final Runnable onChanged;
    private final List<InventorySlot> slots = new ArrayList<>();
    private final Int2ObjectMap<InventorySlot> slotMap = new Int2ObjectOpenHashMap<>();
    
    public SlotAwareItemHandler(Runnable onChanged) {
        this.onChanged = onChanged;
    }
    
    public InventorySlot addSlot(String name, InventorySlot.Mode mode, int size, Predicate<ItemStack> filter, Consumer<ItemStack> onChanged) {
        InventorySlot slot = new InventorySlot(this, name, mode, size, filter, onChanged);
        this.slots.add(slot);
        int index = this.slots.indexOf(slot);
        for (int i = index; i < index + size; i++) {
            this.slotMap.put(i, slot);
        }
        return slot;
    }
    
    public void onChanged() {
        this.onChanged.run();
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        validateSlotIndex(slot);
        this.slotMap.get(slot).setItem(slot, stack);
    }

    @Override
    public int getSlots() {
        return this.slots.size();
    }

    @NotNull
    @Override
    public ItemStack getStackInSlot(int slot) {
        validateSlotIndex(slot);
        return this.slotMap.get(slot).get(slot);
    }

    @NotNull
    @Override
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        validateSlotIndex(slot);
        InventorySlot invSlot = this.slotMap.get(slot);
        if (invSlot.canInsert(stack)) {
            if (!simulate) {
                invSlot.setItem(slot, stack);
            }
            return stack;
        }
        return ItemStack.EMPTY;
    }

    @NotNull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        validateSlotIndex(slot);
        InventorySlot invSlot = this.slotMap.get(slot);
        if (invSlot.canExtract(amount)) {
            return invSlot.extract(slot, amount);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        validateSlotIndex(slot);
        return 64;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        validateSlotIndex(slot);
        return this.slots.get(slot).canInsert(stack);
    }
    
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        this.slots.forEach(slot -> nbt.put(slot.getName(),  slot.serializeNBT()));
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.slots.forEach(slot -> {
            if (nbt.contains(slot.getName())) {
                CompoundTag tag = nbt.getCompound(slot.getName());
                slot.deserializeNBT(tag);
            }
        });
    }

    protected void validateSlotIndex(int slot) {
        if (slot < 0 || slot >= this.slots.size())
            throw new RuntimeException("Slot " + slot + " not in valid range - [0," + this.slots.size() + ")");
    }
}
