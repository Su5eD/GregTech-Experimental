package dev.su5ed.gregtechmod.object;

import dev.su5ed.gregtechmod.item.ResourceItem;
import dev.su5ed.gregtechmod.util.ItemProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.Lazy;

import java.util.Locale;
import java.util.function.Supplier;

import static dev.su5ed.gregtechmod.api.util.Reference.location;

public enum Nugget implements ItemProvider {
    ALUMINIUM(Ingot.ALUMINIUM.description),
    ANTIMONY(Ingot.ANTIMONY.description),
    BRASS(Ingot.BRASS.description),
    CHROME(Ingot.CHROME.description),
    COPPER("Cu"),
    ELECTRUM(Ingot.ELECTRUM.description),
    INVAR(Ingot.INVAR.description),
    IRIDIUM(Ingot.IRIDIUM.description),
    LEAD("Pg"),
    NICKEL(Ingot.NICKEL.description),
    OSMIUM(Ingot.OSMIUM.description),
    PLATINUM(Ingot.PLATINUM.description),
    SILVER("Ag"),
    STEEL("Fe"),
    TIN("Sn"),
    TITANIUM(Ingot.TITANIUM.description),
    TUNGSTEN(Ingot.TUNGSTEN.description),
    ZINC(Ingot.ZINC.description);

    private final Lazy<Item> instance;
    public final Supplier<String> description;

    Nugget(String description) {
        this(() -> description);
    }

    Nugget(Supplier<String> description) {
        this.description = description;

        ResourceLocation name = location(getName() + "_nugget");
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
