package dev.su5ed.gtexperimental.util.inventory;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.ItemHandlerHelper;
import one.util.streamex.StreamEx;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class InventorySlot implements INBTSerializable<CompoundTag> {
    private final SlotAwareItemHandler parent;
    private final String name;
    private final Mode mode;
    private final Predicate<ItemStack> filter;
    private final Consumer<ItemStack> onChanged;

    private final ItemStack[] content;

    public InventorySlot(SlotAwareItemHandler parent, String name, Mode mode, int size, Predicate<ItemStack> filter, Consumer<ItemStack> onChanged) {
        this.parent = parent;
        this.name = name;
        this.mode = mode;
        this.filter = filter;
        this.onChanged = onChanged;
        this.content = new ItemStack[size];
        Arrays.fill(this.content, ItemStack.EMPTY);
    }

    public String getName() {
        return this.name;
    }

    public int getSize() {
        return this.content.length;
    }

    public Collection<ItemStack> getContent() {
        return List.of(this.content);
    }

    public boolean canPlace(ItemStack stack) {
        return this.mode.place && accepts(stack);
    }

    public boolean canInsert(ItemStack stack) {
        return this.mode.insert && accepts(stack);
    }

    public boolean canTake() {
        return this.mode.take;
    }

    public boolean canExtract(int amount) {
        return this.mode.extract;
    }

    public boolean accepts(ItemStack stack) {
        return this.filter.test(stack);
    }

    public ItemStack add(int index, ItemStack stack) {
        return add(index, stack, false);
    }

    /**
     * @return the insertion remainder
     */
    public ItemStack add(int index, ItemStack stack, boolean simulate) {
        if (!stack.isEmpty() && canAdd(index, stack)) {
            ItemStack existing = get(index);
            if (existing.isEmpty()) {
                if (!simulate) {
                    setItem(index, stack);
                }
                return ItemStack.EMPTY;
            }
            if (!simulate) {
                int total = Math.min(existing.getCount() + stack.getCount(), existing.getMaxStackSize());
                existing.setCount(total);
            }
            int remainder = existing.getCount() + stack.getCount() - existing.getMaxStackSize();
            return remainder > 0 ? ItemHandlerHelper.copyStackWithSize(stack, remainder) : ItemStack.EMPTY;
        }
        return stack;
    }

    private boolean canAdd(int index, ItemStack stack) {
        if (accepts(stack)) {
            ItemStack existing = get(index);
            return existing.isEmpty() || ItemHandlerHelper.canItemStacksStack(existing, stack);
        }
        return false;
    }

    public ItemStack get() {
        return get(0);
    }

    public ItemStack get(int index) {
        return this.content[index];
    }

    public void setItem(int index, ItemStack stack) {
        setItem(index, stack, true);
    }

    public void setItem(int index, ItemStack stack, boolean notify) {
        this.content[index] = stack;
        onChanged(stack, notify);
    }

    public void shrink(int index, int amount) {
        get(index).shrink(amount);
    }

    public ItemStack take(int index, int amount) {
        if (canTake()) {
            ItemStack stack = get(index).split(amount);
            onChanged(stack, true);
            return stack;
        }
        return ItemStack.EMPTY;
    }

    public ItemStack extract(int index, int amount, boolean simulate) {
        if (canExtract(amount)) {
            ItemStack existing = get(index);
            if (!simulate) {
                ItemStack stack = existing.split(amount);
                onChanged(stack, true);
                return stack;
            }
            return ItemHandlerHelper.copyStackWithSize(existing, Math.min(amount, existing.getCount()));
        }
        return ItemStack.EMPTY;
    }

    public boolean isEmpty() {
        return StreamEx.of(this.content).allMatch(ItemStack::isEmpty);
    }

    protected void onChanged(ItemStack stack, boolean notify) {
        if (notify) {
            this.parent.onChanged();
        }
        this.onChanged.accept(stack);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        for (int i = 0; i < this.content.length; i++) {
            ItemStack stack = this.content[i];
            if (!stack.isEmpty()) {
                tag.put(String.valueOf(i), stack.save(new CompoundTag()));
            }
        }
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        for (int i = 0; i < this.content.length; i++) {
            String key = String.valueOf(i);
            if (tag.contains(key)) {
                this.content[i] = ItemStack.of(tag.getCompound(key));
            }
        }
    }

    public enum Mode {
        INPUT(true, true, true, false),
        OUTPUT(false, false, true, true),
        BOTH(true, true, true, true),
        NONE(false, false, false, false);

        private final boolean place;
        private final boolean insert;
        private final boolean take;
        private final boolean extract;

        Mode(boolean place, boolean insert, boolean take, boolean extract) {
            this.place = place;
            this.insert = insert;
            this.take = take;
            this.extract = extract;
        }
    }
}
