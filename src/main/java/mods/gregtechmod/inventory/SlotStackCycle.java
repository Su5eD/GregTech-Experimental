package mods.gregtechmod.inventory;

import ic2.core.block.invslot.InvSlot;
import ic2.core.slot.SlotInvSlot;
import mods.gregtechmod.util.ButtonClick;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public class SlotStackCycle extends SlotInvSlot implements ISlotInteractive {
    private final List<ItemStack> stacks;
    private final Function<List<ItemStack>, Integer> supplier;
    
    public SlotStackCycle(InvSlot invSlot, int index, int x, int y, List<ItemStack> stacks) {
        this(invSlot, index, x, y, stacks, list -> 0);
    }

    public SlotStackCycle(InvSlot invSlot, int index, int x, int y, List<ItemStack> stacks, Function<List<ItemStack>, Integer> supplier) {
        super(invSlot, index, x, y);

        this.stacks = stacks;
        this.supplier = supplier;
    }

    @Override
    public boolean slotClick(ButtonClick click, EntityPlayer player, ItemStack stack) {
        ItemStack content = getStack();

        if (click == ButtonClick.SHIFT_MOVE) putStack(ItemStack.EMPTY);
        else if (click == ButtonClick.MOUSE_LEFT) {
            if (content.isEmpty()) putStack(this.stacks.get(this.supplier.apply(this.stacks)).copy());
            else putStack(getNextStack(content));
        }

        return true;
    }
    
    public ItemStack getNextStack(ItemStack offset) {
        if (!offset.isEmpty()) {
            Iterator<ItemStack> it = this.stacks.iterator();
            while (it.hasNext()) {
                ItemStack stack = it.next();
                if (GtUtil.stackEquals(stack, offset) && it.hasNext()) return it.next().copy();
            }
        }
        
        return ItemStack.EMPTY;
    }
}
