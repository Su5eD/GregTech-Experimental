package dev.su5ed.gtexperimental.util.inventory;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import one.util.streamex.StreamEx;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class SlotAwareItemHandler implements IItemHandler, IItemHandlerModifiable, INBTSerializable<CompoundTag> {
    private final Runnable onChanged;
    private final List<InventorySlot> slots = new ArrayList<>();
    private final Int2ObjectMap<SlotIndex> slotMap = new Int2ObjectOpenHashMap<>();

    public SlotAwareItemHandler(Runnable onChanged) {
        this.onChanged = onChanged;
    }

    public InventorySlot addSlot(String name, InventorySlot.Mode mode, int size, Predicate<ItemStack> filter, Consumer<ItemStack> onChanged) {
        return addSlot(new InventorySlot(this, name, mode, size, filter, onChanged));
    }

    public <T extends InventorySlot> T addSlot(T slot) {
        this.slots.add(slot);
        int index = this.slots.indexOf(slot);
        for (int i = 0; i < slot.getSize(); i++) {
            this.slotMap.put(i + index, new SlotIndex(slot, i));
        }
        return slot;
    }

    public void onChanged() {
        this.onChanged.run();
    }

    public Collection<ItemStack> getAllItems() {
        return StreamEx.of(this.slots)
            .flatCollection(InventorySlot::getContent)
            .remove(ItemStack::isEmpty)
            .toList();
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        validateSlotIndex(slot);
        this.slotMap.get(slot).slot.setItem(slot, stack);
    }

    @Override
    public int getSlots() {
        return this.slots.size();
    }

    @NotNull
    @Override
    public ItemStack getStackInSlot(int slot) {
        validateSlotIndex(slot);
        SlotIndex invSlot = this.slotMap.get(slot);
        return invSlot.slot.get(invSlot.index);
    }

    @NotNull
    @Override
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        validateSlotIndex(slot);
        SlotIndex invSlot = this.slotMap.get(slot);
        if (invSlot.slot.canInsert(stack)) {
            return invSlot.slot.add(invSlot.index, stack, simulate);
        }
        return stack;
    }

    @NotNull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        validateSlotIndex(slot);
        SlotIndex invSlot = this.slotMap.get(slot);
        return invSlot.slot.extract(invSlot.index, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        validateSlotIndex(slot);
        return 64;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        validateSlotIndex(slot);
        return this.slots.get(slot).accepts(stack);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        this.slots.forEach(slot -> nbt.put(slot.getName(), slot.serializeNBT()));
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
        if (slot < 0 || slot >= this.slots.size()) {
            throw new RuntimeException("Slot " + slot + " not in valid range - [0," + this.slots.size() + ")");
        }
    }

    private record SlotIndex(InventorySlot slot, int index) {}
}
