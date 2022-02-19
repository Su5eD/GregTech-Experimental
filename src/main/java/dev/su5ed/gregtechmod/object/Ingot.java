package dev.su5ed.gregtechmod.object;

import dev.su5ed.gregtechmod.item.ResourceItem;
import dev.su5ed.gregtechmod.util.GtLocale;
import dev.su5ed.gregtechmod.util.ItemProvider;
import dev.su5ed.gregtechmod.util.JavaUtil;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.Lazy;

import java.util.function.Supplier;

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
    TUNGSTEN_STEEL(() -> GtLocale.translateItemDescription("tungsten_steel_ingot")),
    ZINC("Zn");

    private final Lazy<Item> instance;
    final Supplier<String> description;

    Ingot() {
        this(JavaUtil.nullSupplier());
    }

    Ingot(String description) {
        this(description, false);
    }

    Ingot(String description, boolean isFoil) {
        this(() -> description, isFoil);
    }

    Ingot(Supplier<String> description) {
        this(description, false);
    }

    Ingot(Supplier<String> description, boolean isFoil) {
        this.description = description;

        this.instance = Lazy.of(() -> new ResourceItem(ModObjects.DEFAULT_ITEM_PROPERTIES, this.description).setIsFoil(isFoil).registryName(getName(), "ingot"));
    }

    @Override
    public Item getItem() {
        return this.instance.get();
    }
}