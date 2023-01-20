package dev.su5ed.gtexperimental.item;

import dev.su5ed.gtexperimental.compat.ModHandler;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class DebugScannerItem extends ScannerItem {

    public DebugScannerItem() {
        super(new ElectricItemProperties()
            .maxCharge(1000000000)
            .energyTier(4)
            .showTier(false));
    }

    @Override
    public void fillItemCategory(CreativeModeTab category, NonNullList<ItemStack> items) {
        if (allowedIn(category)) {
            items.add(ModHandler.getChargedStack(this, Double.MAX_VALUE));
        }
    }
}
