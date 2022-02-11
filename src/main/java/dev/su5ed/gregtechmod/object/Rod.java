package dev.su5ed.gregtechmod.object;

import dev.su5ed.gregtechmod.item.ResourceItem;
import dev.su5ed.gregtechmod.util.ItemProvider;
import ic2.core.profile.NotExperimental;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.Lazy;

import java.util.Locale;
import java.util.function.Supplier;

import static dev.su5ed.gregtechmod.api.util.Reference.location;

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
    @NotExperimental
    REFINED_IRON("Fe"),
    SILVER(Ingot.SILVER.description),
    STEEL(Ingot.STEEL.description),
    TIN("Sn"),
    TITANIUM(Ingot.TITANIUM.description),
    TUNGSTEN(Ingot.TUNGSTEN.description),
    TUNGSTEN_STEEL(Ingot.TUNGSTEN_STEEL.description),
    ZINC(Ingot.ZINC.description);

    private final Lazy<Item> instance;
    public final Supplier<String> description;

    Rod(String description) {
        this(() -> description);
    }

    Rod(Supplier<String> description) {
        this.description = description;

        ResourceLocation name = location(getName() + "_rod");
        this.instance = Lazy.of(() -> new ResourceItem(ModObjects.ITEM_PROPERTIES, this.description).setRegistryName(name));
    }

    @Override
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }

    @Override
    public Item getItem() {
        return this.instance.get();
    }
}
