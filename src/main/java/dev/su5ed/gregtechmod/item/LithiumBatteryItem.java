package dev.su5ed.gregtechmod.item;

import dev.su5ed.gregtechmod.object.ModObjects;
import net.minecraft.resources.ResourceLocation;

import static dev.su5ed.gregtechmod.api.util.Reference.location;

public class LithiumBatteryItem extends ResourceItem {
    public static final ResourceLocation CHARGE_PROPERTY = location("charge");

    public LithiumBatteryItem() {
        super(ModObjects.DEFAULT_ITEM_PROPERTIES); // 100000, 128, 1 TODO Extends ItemBattery when IC2 works
    }
}
