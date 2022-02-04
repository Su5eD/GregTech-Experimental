package mods.gregtechmod.inventory;

import ic2.core.block.invslot.InvSlot;
import mods.gregtechmod.util.ButtonClick;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class SlotInvSlotSizeFilter extends SlotInvSlotHolo {

    public SlotInvSlotSizeFilter(InvSlot invSlot, int index, int x, int y) {
        super(invSlot, index, x, y);
    }

    @Override
    public boolean slotClick(ButtonClick click, EntityPlayer player, ItemStack stack) {
        ItemStack content = getStack();

        if (click == ButtonClick.SHIFT_MOVE) putStack(ItemStack.EMPTY);
        else if (!content.isEmpty()) {
            if (click == ButtonClick.MOUSE_LEFT) content.shrink(1);
            else if (click == ButtonClick.MOUSE_RIGHT) content.setCount(Math.min(content.getMaxStackSize(), content.getCount() + 1));
        }
        else if (!stack.isEmpty()) {
            putStack(stack.isItemDamaged() ? GtUtil.copyWithoutDamage(stack, stack.getCount()) : stack.copy());
        }

        return true;
    }
}
