package dev.su5ed.gtexperimental.util.inventory;

import dev.su5ed.gtexperimental.api.util.DischargingSlot;
import dev.su5ed.gtexperimental.compat.ModHandler;
import net.minecraft.world.item.ItemStack;

public class DischargingInventorySlot extends InventorySlot implements DischargingSlot {
    private final int tier;

    public DischargingInventorySlot(SlotAwareItemHandler parent, String name, Mode mode, int tier) {
        super(parent, name, mode, 1, stack -> ModHandler.dischargeStack(stack, Double.POSITIVE_INFINITY, tier, true, true, true) > 0, stack -> {});
        
        this.tier = tier;
    }

    @Override
    public double discharge(double energy, boolean simulate) {
        ItemStack stack = get(0);
        return !stack.isEmpty() ? ModHandler.dischargeStack(stack, energy, this.tier, simulate, true, false) : 0;
    }
}
