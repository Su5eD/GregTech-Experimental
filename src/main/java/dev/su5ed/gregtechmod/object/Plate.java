package dev.su5ed.gregtechmod.object;

import dev.su5ed.gregtechmod.item.ResourceItem;
import dev.su5ed.gregtechmod.item.ResourceItem.ExtendedItemProperties;
import dev.su5ed.gregtechmod.util.ItemProvider;
import ic2.core.profile.NotExperimental;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.Lazy;

public enum Plate implements ItemProvider {
    ALUMINIUM(Ingot.ALUMINIUM.description),
    BATTERY_ALLOY(Ingot.BATTERY_ALLOY.description),
    BRASS(Ingot.BRASS.description),
    BRONZE(Rod.BRONZE.description),
    CHROME(Ingot.CHROME.description),
    COPPER(Rod.COPPER.description),
    ELECTRUM(Ingot.ELECTRUM.description),
    GOLD(Rod.GOLD.description),
    INVAR(Ingot.INVAR.description),
    IRIDIUM(Ingot.IRIDIUM.description),
    IRON("Fe"),
    LEAD(Ingot.LEAD.description),
    MAGNALIUM(Ingot.MAGNALIUM.description),
    NICKEL(Ingot.NICKEL.description),
    OSMIUM(Ingot.OSMIUM.description),
    PLATINUM(Ingot.PLATINUM.description),
    @NotExperimental
    REFINED_IRON("Fe"),
    SILICON("Si2"),
    SILVER(Ingot.SILVER.description),
    STEEL(Ingot.STEEL.description),
    TIN(Rod.TIN.description),
    TITANIUM(Ingot.TITANIUM.description),
    TUNGSTEN(Ingot.TUNGSTEN.description),
    TUNGSTEN_STEEL(Ingot.TUNGSTEN_STEEL.description),
    WOOD,
    ZINC(Ingot.ZINC.description);

    private final Lazy<Item> instance;
    
    Plate() {
        this((MutableComponent) null);
    }
        
    Plate(String description) {
        this(new TextComponent(description));
    }

    Plate(MutableComponent description) {
        this.instance = Lazy.of(() -> new ResourceItem(new ExtendedItemProperties<>().description(description)).registryName(getName(), "plate"));
    }

    @Override
    public Item getItem() {
        return this.instance.get();
    }
}
