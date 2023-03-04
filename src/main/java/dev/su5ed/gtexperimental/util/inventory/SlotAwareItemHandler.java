package dev.su5ed.gtexperimental.util.inventory;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;
import one.util.streamex.StreamEx;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class SlotAwareItemHandler implements SidedItemHandler, INBTSerializable<CompoundTag> {
    private final Runnable onChanged;
    private final List<InventorySlot> slots = new ArrayList<>();
    private final Int2ObjectMap<SlotIndex> slotMap = new Int2ObjectOpenHashMap<>();

    public SlotAwareItemHandler(Runnable onChanged) {
        this.onChanged = onChanged;
    }

    public InventorySlot addSlot(String name, InventorySlot.Mode mode, SlotDirection side, int size, Predicate<ItemStack> filter, Consumer<ItemStack> onChanged) {
        return addSlot(new InventorySlot(this, name, mode, side, size, filter, onChanged));
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
    public void setStackInSlot(int slot, ItemStack stack, @Nullable Direction side) {
        validateSlotIndex(slot);
        this.slotMap.get(slot).slot.setItem(slot, stack);
    }

    @Override
    public int getSlots(@Nullable Direction side) {
        return this.slots.size();
    }

    @Override
    public ItemStack getStackInSlot(int slot, @Nullable Direction side) {
        validateSlotIndex(slot);
        SlotIndex invSlot = this.slotMap.get(slot);
        return invSlot.slot.get(invSlot.index);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, @Nullable Direction side, boolean simulate) {
        validateSlotIndex(slot);
        SlotIndex invSlot = this.slotMap.get(slot);
        return invSlot.slot.add(invSlot.index, stack, side, simulate);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, @Nullable Direction side, boolean simulate) {
        validateSlotIndex(slot);
        SlotIndex invSlot = this.slotMap.get(slot);
        return invSlot.slot.extract(invSlot.index, amount, side, simulate);
    }

    @Override
    public int getSlotLimit(int slot, @Nullable Direction side) {
        validateSlotIndex(slot);
        return 64;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack, @Nullable Direction side) {
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
