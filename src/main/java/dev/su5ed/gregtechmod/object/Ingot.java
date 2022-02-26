package dev.su5ed.gregtechmod.object;

import dev.su5ed.gregtechmod.item.ResourceItem;
import dev.su5ed.gregtechmod.item.ResourceItem.ExtendedItemProperties;
import dev.su5ed.gregtechmod.util.GtLocale;
import dev.su5ed.gregtechmod.util.ItemProvider;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.Lazy;

public enum Ingot implements ItemProvider {
    ALUMINIUM("Al"),
    ANTIMONY("Sb"),
    BATTERY_ALLOY("Pb4Sb1"),
    BRASS("ZnCu3"),
    CHROME("Cr"),
    ELECTRUM("AgAu"),
    HOT_TUNGSTEN_STEEL,
    INVAR("Fe2Ni"),
    IRIDIUM("Ir"),
    IRIDIUM_ALLOY,
    LEAD("Pb"),
    MAGNALIUM("MgAl2"),
    NICKEL("Ni"),
    OSMIUM("Os"),
    PLATINUM("Pt"),
    PLUTONIUM("Pu", true),
    SILVER("Ag"),
    SOLDERING_ALLOY("Sn9Sb1"),
    STEEL("Fe"),
    THORIUM("Th", true),
    TITANIUM("Ti"),
    TUNGSTEN("W"),
    TUNGSTEN_STEEL(GtLocale.itemDescriptionKey("tungsten_steel_ingot").toComponent(), false),
    ZINC("Zn");

    private final Lazy<Item> instance;
    final MutableComponent description;

    Ingot() {
        this((MutableComponent) null, false);
    }

    Ingot(String description) {
        this(description, false);
    }

    Ingot(String description, boolean isFoil) {
        this(new TextComponent(description), isFoil);
    }

    Ingot(MutableComponent description, boolean isFoil) {
        this.description = description;
        this.instance = Lazy.of(() -> new ResourceItem(new ExtendedItemProperties<>().description(description).foil(isFoil)).registryName(getName(), "ingot"));
    }

    @Override
    public Item getItem() {
        return this.instance.get();
    }
}
