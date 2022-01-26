package mods.gregtechmod.inventory;

import mods.gregtechmod.inventory.invslot.GtSlotCraftingGrid;
import mods.gregtechmod.util.ButtonClick;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class SlotCraftingGrid extends SlotInvSlotHolo {

    public SlotCraftingGrid(GtSlotCraftingGrid invSlot, int index, int x, int y) {
        super(invSlot, index, x, y);
    }

    @Override
    public boolean canTakeStack(EntityPlayer player) {
        return this.invSlot.canOutput();
    }

    @Override
    public boolean slotClick(ButtonClick click, EntityPlayer player, ItemStack stack) {
        return this.invSlot.canInput() && super.slotClick(click, player, stack);
    }
}
