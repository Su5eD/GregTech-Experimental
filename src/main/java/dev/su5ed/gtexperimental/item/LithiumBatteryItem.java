package dev.su5ed.gtexperimental.item;

import net.minecraft.resources.ResourceLocation;

import static dev.su5ed.gtexperimental.util.GtUtil.location;

public class LithiumBatteryItem extends ElectricItem {
    public static final ResourceLocation CHARGE_PROPERTY = location("charge");

    public LithiumBatteryItem() {
        super(new ElectricItemProperties()
            .maxCharge(100000)
            .transferLimit(128)
            .energyTier(1)
            .providesEnergy(true)
            .stacksTo(16));
    }
}
