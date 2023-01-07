package dev.su5ed.gregtechmod.util.inventory;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;
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
    
    public boolean canInsert(ItemStack stack) {
        return this.mode.input && this.filter.test(stack);
    }
    
    public boolean canExtract(int amount) {
        return this.mode.output;
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
        INPUT(true, false),
        OUTPUT(false, true),
        BOTH(true, true),
        NONE(false, false);
        
        private final boolean input;
        private final boolean output;

        Mode(boolean input, boolean output) {
            this.input = input;
            this.output = output;
        }
    }
}
