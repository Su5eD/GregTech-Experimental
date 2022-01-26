package mods.gregtechmod.inventory;

import ic2.core.block.invslot.InvSlot;
import ic2.core.slot.SlotInvSlot;
import ic2.core.util.StackUtil;
import mods.gregtechmod.util.ButtonClick;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class SlotInvSlotHolo extends SlotInvSlot implements ISlotInteractive {

    public SlotInvSlotHolo(InvSlot invSlot, int index, int x, int y) {
        super(invSlot, index, x, y);
    }

    @Override
    public boolean canTakeStack(EntityPlayer player) {
        return false;
    }

    @Override
    public boolean slotClick(ButtonClick click, EntityPlayer player, ItemStack stack) {
        putStack(!stack.isEmpty() ? stack.isItemDamaged() ? GtUtil.copyWithoutDamage(stack, 1) : StackUtil.copyWithSize(stack, 1) : ItemStack.EMPTY);
        return true;
    }
}
