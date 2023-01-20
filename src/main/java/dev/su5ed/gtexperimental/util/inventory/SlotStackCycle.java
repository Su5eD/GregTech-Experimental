package dev.su5ed.gtexperimental.util.inventory;

import dev.su5ed.gtexperimental.util.GtUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public class SlotStackCycle extends SlotReadOnly implements InteractiveSlot {
    private final List<ItemStack> stacks;
    private final Function<List<ItemStack>, Integer> supplier;

    public SlotStackCycle(InventorySlot inventorySlot, int index, int x, int y, List<ItemStack> stacks) {
        this(inventorySlot, index, x, y, stacks, list -> 0);
    }
    
    public SlotStackCycle(InventorySlot inventorySlot, int index, int x, int y, List<ItemStack> stacks, Function<List<ItemStack>, Integer> supplier) {
        super(inventorySlot, index, x, y);
        
        this.stacks = stacks;
        this.supplier = supplier;
    }

    @Override
    public void slotClick(ButtonClick click, Player player, ItemStack stack) {
        if (click == ButtonClick.SHIFT_MOVE) {
            set(ItemStack.EMPTY);
        }
        else if (click == ButtonClick.MOUSE_LEFT) {
            setNextStack();
        }
    }

    @Override
    public void slotScroll(Player player, ScrollDirection direction, boolean shift) {
        if (direction == ScrollDirection.DOWN) {
            setNextStack();
        }
        else {
            setPreviousStack();
        }   
    }

    public void setPreviousStack() {
        ItemStack content = getItem();
        set(getPreviousStack(content));
    }
    
    public void setNextStack() {
        ItemStack content = getItem();
        set(getNextStack(content));
    }

    public ItemStack getPreviousStack(ItemStack offset) {
        if (!offset.isEmpty()) {
            int index = -1;
            for (int i = 0; i < this.stacks.size(); i++) {
                ItemStack stack = this.stacks.get(i);
                if (GtUtil.stackEquals(stack, offset)) {
                    index = i;
                    break;
                }
            }
            return index > 0 ? this.stacks.get(index - 1).copy() : ItemStack.EMPTY;
        }
        return this.stacks.get(this.stacks.size() - 1).copy();
    }

    public ItemStack getNextStack(ItemStack offset) {
        if (!offset.isEmpty()) {
            Iterator<ItemStack> it = this.stacks.iterator();
            while (it.hasNext()) {
                ItemStack stack = it.next();
                if (GtUtil.stackEquals(stack, offset) && it.hasNext()) {
                    return it.next().copy();
                }
            }
            return ItemStack.EMPTY;
        }
        return this.stacks.get(this.supplier.apply(this.stacks)).copy();
    }
}
