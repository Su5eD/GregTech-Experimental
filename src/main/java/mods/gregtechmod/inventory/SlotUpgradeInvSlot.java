package mods.gregtechmod.inventory;

import ic2.core.block.invslot.InvSlot;
import ic2.core.slot.SlotInvSlot;

public class SlotUpgradeInvSlot extends SlotInvSlot {

    public SlotUpgradeInvSlot(InvSlot invSlot, int index, int x, int y) {
        super(invSlot, index, x, y);
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
