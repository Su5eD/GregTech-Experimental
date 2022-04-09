package mods.gregtechmod.inventory;

import ic2.core.block.invslot.InvSlot;
import mods.gregtechmod.util.ButtonClick;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

public class SlotInvSlotHolo extends SlotReadOnly implements ISlotInteractive {

    public SlotInvSlotHolo(InvSlot invSlot, int index, int x, int y) {
        super(invSlot, index, x, y);
    }

    @Override
    public boolean slotClick(ButtonClick click, EntityPlayer player, ItemStack stack) {
        if (stack.isEmpty()) putStack(ItemStack.EMPTY);
        else putStack(stack.isItemDamaged() ? GtUtil.copyWithoutDamage(stack, 1) : ItemHandlerHelper.copyStackWithSize(stack, 1));
        return true;
    }
}
