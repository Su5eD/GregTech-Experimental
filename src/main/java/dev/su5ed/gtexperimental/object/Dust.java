package dev.su5ed.gtexperimental.object;

import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.item.ResourceItem;
import dev.su5ed.gtexperimental.item.ResourceItem.ExtendedItemProperties;
import dev.su5ed.gtexperimental.util.GtUtil;
import dev.su5ed.gtexperimental.util.TaggedItemProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;

public enum Dust implements TaggedItemProvider {
    ALMANDINE("Al2Fe3Si3O12"),
    ALUMINIUM(Ingot.ALUMINIUM.description),
    ANDRADITE("Ca3Fe2Si3O12"),
    ANTIMONY(Ingot.ANTIMONY.description),
    ASHES("C"),
    BASALT("(Mg2Fe2SiO4)(CaCO3)3(SiO2)8C4"),
    BAUXITE("TiAl16H10O12"),
    BRASS(Ingot.BRASS.description),
    CALCITE("CaCO3"),
    CHARCOAL("C"),
    CHROME(Ingot.CHROME.description),
    CINNABAR("HgS"),
    CLAY("Na2LiAl2Si2"),
    DARK_ASHES("C"),
    DIAMOND("C128"),
    ELECTRUM(Ingot.ELECTRUM.description),
    EMERALD("Be3Al2Si6O18"),
    ENDER_EYE("BeK4N5Cl6C4S2"),
    ENDER_PEARL("BeK4N5Cl6"),
    ENDSTONE,
    FLINT("SiO2"),
    GALENA("Pb3Ag3S2"),
    GREEN_SAPPHIRE("Al206"),
    GROSSULAR("Ca3Al2Si3O12"),
    INVAR(Ingot.INVAR.description),
    LAZURITE("Al6Si6Ca8Na8"),
    LEAD(Ingot.LEAD.description),
    MAGNESIUM("Mg"),
    MANGANESE("Mn"),
    MARBLE("Mg(CaCO3)7"),
    NETHERRACK,
    NICKEL(Ingot.NICKEL.description),
    OBSIDIAN("MgFeSiO8"),
    OLIVINE("Mg2Fe2SiO4"),
    OSMIUM(Ingot.OSMIUM.description),
    PHOSPHORUS("Ca3(PO4)2"),
    PLATINUM(Ingot.PLATINUM.description),
    PLUTONIUM(Ingot.PLUTONIUM.description, true),
    PYRITE("FeS2"),
    PYROPE("Al2Mg3Si3O12"),
    RED_GARNET("(Al2Mg3Si3O12)3(Al2Fe3Si3O12)5(Al2Mn3Si3O12)8"),
    REDROCK("(Na2LiAl2Si2)((CaCO3)2SiO2)3"),
    RUBY("Al206Cr"),
    SALTPETER("KNO3"),
    SAPPHIRE("Al206"),
    SODALITE("Al3Si3Na4Cl"),
    SPESSARTINE("Al2Mn3Si3O12"),
    SPHALERITE("ZnS"),
    SILVER(Ingot.SILVER.description),
    STEEL(Ingot.STEEL.description),
    SULFUR("S"),
    THORIUM(Ingot.THORIUM.description, true),
    TITANIUM(Ingot.TITANIUM.description),
    TUNGSTEN(Ingot.TUNGSTEN.description),
    URANIUM("U", true),
    UVAROVITE("Ca3Cr2Si3O12"),
    WOOD,
    YELLOW_GARNET("(Ca3Fe2Si3O12)5(Ca3Al2Si3O12)8(Ca3Cr2Si3O12)3"),
    ZINC(Ingot.ZINC.description);

    private final Lazy<Item> instance;
    private final TagKey<Item> tag;
    final MutableComponent description;
    
    Dust() {
        this((MutableComponent) null);
    }

    Dust(String description) {
        this(description, false);
    }
    
    Dust(MutableComponent description) {
        this(description, false);
    }
    
    Dust(String description, boolean isFoil) {
        this(Component.literal(description), isFoil);
    }

    Dust(MutableComponent description, boolean isFoil) {
        this.description = description;
        this.instance = Lazy.of(() -> new ResourceItem(new ExtendedItemProperties<>().description(description).foil(isFoil)));
        this.tag = GregTechTags.dust(getName());
    }
    
    @Override
    public String getRegistryName() {
        return GtUtil.registryName(getName(), "dust");
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
