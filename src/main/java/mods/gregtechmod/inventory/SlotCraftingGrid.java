package mods.gregtechmod.inventory;

import ic2.core.block.invslot.InvSlot;
import mods.gregtechmod.objects.blocks.teblocks.inv.TileEntityElectricCraftingTable;
import mods.gregtechmod.util.ButtonClick;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class SlotCraftingGrid extends SlotInvSlotHolo {

    public SlotCraftingGrid(InvSlot invSlot, int index, int x, int y) {
        super(invSlot, index, x, y);
    }

    @Override
    public boolean slotClick(ButtonClick click, EntityPlayer player, ItemStack stack) {
        return isPatternMode() && super.slotClick(click, player, stack);
    }
    
    private boolean isPatternMode() {
        return ((TileEntityElectricCraftingTable) this.invSlot.base).getCraftingMode() == TileEntityElectricCraftingTable.CraftingMode.PATTERN;
    }
}
