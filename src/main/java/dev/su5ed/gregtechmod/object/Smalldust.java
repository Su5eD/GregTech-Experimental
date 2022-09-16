package dev.su5ed.gregtechmod.object;

import dev.su5ed.gregtechmod.item.ResourceItem;
import dev.su5ed.gregtechmod.item.ResourceItem.ExtendedItemProperties;
import dev.su5ed.gregtechmod.util.GtUtil;
import dev.su5ed.gregtechmod.util.ItemProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.Lazy;

public enum Smalldust implements ItemProvider {
    ALMANDINE(Dust.ALMANDINE.description),
    ALUMINIUM(Dust.ALUMINIUM.description),
    ANDRADITE(Dust.ANDRADITE.description),
    ANTIMONY(Ingot.ANTIMONY.description),
    ASHES("C"),
    BASALT(Dust.BASALT.description),
    BAUXITE(Dust.BAUXITE.description),
    BRASS(Dust.BRASS.description),
    BRONZE(Rod.BRONZE.description),
    CALCITE(Dust.CALCITE.description),
    CHARCOAL(Dust.CHARCOAL.description),
    CHROME(Dust.CHROME.description),
    CINNABAR(Dust.CINNABAR.description),
    CLAY(Dust.CLAY.description),
    COAL("C2"),
    COPPER(Rod.COPPER.description),
    DARK_ASHES(Dust.DARK_ASHES.description),
    DIAMOND(Dust.DIAMOND.description),
    ELECTRUM(Dust.ELECTRUM.description),
    EMERALD(Dust.EMERALD.description),
    ENDER_EYE(Dust.ENDER_EYE.description),
    ENDER_PEARL(Dust.ENDER_PEARL.description),
    ENDSTONE,
    FLINT(Dust.FLINT.description),
    GALENA(Dust.GALENA.description),
    GLOWSTONE,
    GOLD(Rod.GOLD.description),
    GREEN_SAPPHIRE(Dust.GREEN_SAPPHIRE.description),
    GROSSULAR(Dust.GROSSULAR.description),
    GUNPOWDER,
    INVAR(Dust.INVAR.description),
    IRON("Fe"),
    LAZURITE(Dust.LAZURITE.description),
    LEAD(Dust.LEAD.description),
    MAGNESIUM(Dust.MAGNESIUM.description),
    MANGANESE(Dust.MANGANESE.description),
    MARBLE(Dust.MARBLE.description),
    NETHERRACK,
    NICKEL(Dust.NICKEL.description),
    OBSIDIAN(Dust.OBSIDIAN.description),
    OLIVINE(Dust.OLIVINE.description),
    OSMIUM(Dust.OSMIUM.description),
    PHOSPHORUS(Dust.PHOSPHORUS.description),
    PLATINUM(Dust.PLATINUM.description),
    PLUTONIUM(Dust.PLUTONIUM.description, true),
    PYRITE(Dust.PYRITE.description),
    PYROPE(Dust.PYROPE.description),
    RED_GARNET(Dust.RED_GARNET.description),
    REDROCK(Dust.REDROCK.description),
    REDSTONE,
    RUBY(Dust.RUBY.description),
    SALTPETER(Dust.SALTPETER.description),
    SAPPHIRE(Dust.SAPPHIRE.description),
    SILVER(Dust.SILVER.description),
    SODALITE(Dust.SODALITE.description),
    SPESSARTINE(Dust.SPESSARTINE.description),
    SPHALERITE(Dust.SPHALERITE.description),
    STEEL(Dust.STEEL.description),
    SULFUR(Dust.SULFUR.description),
    THORIUM(Dust.THORIUM.description, true),
    TIN(Rod.TIN.description),
    TITANIUM(Dust.TITANIUM.description),
    TUNGSTEN(Dust.TUNGSTEN.description),
    URANIUM(Dust.URANIUM.description, true),
    UVAROVITE(Dust.UVAROVITE.description),
    WOOD(Dust.WOOD.description),
    YELLOW_GARNET(Dust.YELLOW_GARNET.description),
    ZINC(Ingot.ZINC.description);

    private final Lazy<Item> instance;

    Smalldust() {
        this((MutableComponent) null);
    }

    Smalldust(String description) {
        this(description, false);
    }

    Smalldust(MutableComponent description) {
        this(description, false);
    }

    Smalldust(String description, boolean isFoil) {
        this(Component.literal(description), isFoil);
    }

    Smalldust(MutableComponent description, boolean isFoil) {
        this.instance = Lazy.of(() -> new ResourceItem(new ExtendedItemProperties<>().description(description).foil(isFoil)));
    }

    @Override
    public String getRegistryName() {
        return GtUtil.registryName(getName(), "smalldust");
    }

    @Override
    public Item getItem() {
        return this.instance.get();
    }
}
