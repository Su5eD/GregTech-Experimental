package mods.gregtechmod.inventory.invslot;

import ic2.api.energy.tile.IChargingSlot;
import ic2.api.energy.tile.IDischargingSlot;
import ic2.api.info.Info;
import ic2.api.item.ElectricItem;
import ic2.core.block.IInventorySlotHolder;
import ic2.core.block.invslot.InvSlot;
import net.minecraft.item.ItemStack;

import java.util.function.BooleanSupplier;

public class GtSlotChargeHybrid extends InvSlot implements IChargingSlot, IDischargingSlot {
    public int tier;
    private final BooleanSupplier shouldDischarge;

    public GtSlotChargeHybrid(IInventorySlotHolder<?> base, String name, int tier, BooleanSupplier shouldDischarge) {
        super(base, name, Access.IO, 1);

        this.tier = tier;
        this.shouldDischarge = shouldDischarge;
    }

    @Override
    public boolean accepts(ItemStack stack) {
        return !stack.isEmpty() && (ElectricItem.manager.charge(stack, Double.POSITIVE_INFINITY, this.tier, true, true) > 0
            || Info.itemInfo.getEnergyValue(stack) > 0
            || ElectricItem.manager.discharge(stack, Double.POSITIVE_INFINITY, this.tier, true, true, true) > 0);
    }

    @Override
    public double charge(double amount) {
        if (!this.shouldDischarge.getAsBoolean()) {
            ItemStack stack = get();
            if (!stack.isEmpty()) {
                return ElectricItem.manager.charge(stack, amount, this.tier, false, false);
            }
        }
        return 0;
    }

    @Override
    public double discharge(double amount, boolean ignoreLimit) {
        if (this.shouldDischarge.getAsBoolean()) {
            ItemStack stack = get();
            if (!stack.isEmpty()) {
                double realAmount = ElectricItem.manager.discharge(stack, amount, this.tier, ignoreLimit, true, false);
                if (realAmount <= 0) {
                    double energyValue = Info.itemInfo.getEnergyValue(stack);
                    if (energyValue > 0) stack.shrink(1);
                }

                return realAmount;
            }
        }
        return 0;
    }
}
