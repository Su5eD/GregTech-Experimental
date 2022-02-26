package dev.su5ed.gregtechmod.item;

import net.minecraft.resources.ResourceLocation;

import static dev.su5ed.gregtechmod.api.util.Reference.location;

public class LithiumBatteryItem extends ResourceItem {
    public static final ResourceLocation CHARGE_PROPERTY = location("charge");

    public LithiumBatteryItem() {
        super(new ExtendedItemProperties<>()); // 100000, 128, 1 TODO Extends ItemBattery when IC2 works
    }
}
