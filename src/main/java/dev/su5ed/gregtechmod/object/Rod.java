package dev.su5ed.gregtechmod.object;

import dev.su5ed.gregtechmod.item.ResourceItem;
import dev.su5ed.gregtechmod.item.ResourceItem.ExtendedItemProperties;
import dev.su5ed.gregtechmod.util.ClassicOnly;
import dev.su5ed.gregtechmod.util.ItemProvider;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.Lazy;

public enum Rod implements ItemProvider {
    ALUMINIUM(Ingot.ALUMINIUM.description),
    BRASS(Ingot.BRASS.description),
    BRONZE("SnCu3"),
    CHROME(Ingot.CHROME.description),
    COPPER("Cu"),
    ELECTRUM(Ingot.ELECTRUM.description),
    GOLD("Au"),
    INVAR(Ingot.INVAR.description),
    IRIDIUM(Ingot.IRIDIUM.description),
    IRON("Fe"),
    LEAD(Ingot.LEAD.description),
    NICKEL(Ingot.NICKEL.description),
    OSMIUM(Ingot.OSMIUM.description),
    PLATINUM(Ingot.PLATINUM.description),
    @ClassicOnly
    REFINED_IRON("Fe"),
    SILVER(Ingot.SILVER.description),
    STEEL(Ingot.STEEL.description),
    TIN("Sn"),
    TITANIUM(Ingot.TITANIUM.description),
    TUNGSTEN(Ingot.TUNGSTEN.description),
    TUNGSTEN_STEEL(Ingot.TUNGSTEN_STEEL.description),
    ZINC(Ingot.ZINC.description);

    private final Lazy<Item> instance;
    public final MutableComponent description;

    Rod(String description) {
        this(new TextComponent(description));
    }

    Rod(MutableComponent description) {
        this.description = description;

        this.instance = Lazy.of(() -> new ResourceItem(new ExtendedItemProperties<>().description(description)).registryName(getName(), "rod"));
    }

    @Override
    public Item getItem() {
        return this.instance.get();
    }
}
