package dev.su5ed.gregtechmod.object;

import dev.su5ed.gregtechmod.GregTechTags;
import dev.su5ed.gregtechmod.item.ResourceItem;
import dev.su5ed.gregtechmod.item.ResourceItem.ExtendedItemProperties;
import dev.su5ed.gregtechmod.util.GtLocale;
import dev.su5ed.gregtechmod.util.GtUtil;
import dev.su5ed.gregtechmod.util.TaggedItemProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;

public enum Ingot implements TaggedItemProvider {
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
    private final TagKey<Item> tag;
    final MutableComponent description;

    Ingot() {
        this((MutableComponent) null, false);
    }

    Ingot(String description) {
        this(description, false);
    }

    Ingot(String description, boolean isFoil) {
        this(Component.literal(description), isFoil);
    }

    Ingot(MutableComponent description, boolean isFoil) {
        this.description = description;
        this.instance = Lazy.of(() -> new ResourceItem(new ExtendedItemProperties<>().description(description).foil(isFoil)));
        this.tag = GregTechTags.material("ingots", getName());
    }

    @Override
    public String getRegistryName() {
        return GtUtil.registryName(getName(), "ingot");
    }

    @Override
    public Item getItem() {
        return this.instance.get();
    }

    @Nullable
    @Override
    public TagKey<Item> getTag() {
        return this.tag;
    }
}
