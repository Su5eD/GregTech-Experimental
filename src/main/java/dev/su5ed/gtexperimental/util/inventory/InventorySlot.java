package dev.su5ed.gtexperimental.util.inventory;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.ItemHandlerHelper;
import one.util.streamex.StreamEx;

import java.util.Arrays;
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

    public boolean canPlace(ItemStack stack) {
        return this.mode.place && accepts(stack);
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

    public boolean canAdd(int index, ItemStack stack) {
        if (accepts(stack)) {
            ItemStack existing = get(index);
            return existing.isEmpty() || ItemHandlerHelper.canItemStacksStack(existing, stack) && existing.getCount() + stack.getCount() <= existing.getMaxStackSize();
        }
        return false;
    }

    public void add(int index, ItemStack stack) {
        if (canAdd(index, stack)) {
            ItemStack existing = get(index);
            if (existing.isEmpty()) {
                setItem(index, stack);
            }
            else {
                existing.grow(stack.getCount());
            }
        }
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

    public ItemStack extract(int index, int amount) {
        ItemStack stack = this.content[index].split(amount);
        onChanged(stack, true);
        return stack;
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
