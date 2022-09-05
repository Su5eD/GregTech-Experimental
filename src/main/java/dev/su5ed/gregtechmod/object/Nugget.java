package dev.su5ed.gregtechmod.object;

import dev.su5ed.gregtechmod.GregTechTags;
import dev.su5ed.gregtechmod.item.ResourceItem;
import dev.su5ed.gregtechmod.item.ResourceItem.ExtendedItemProperties;
import dev.su5ed.gregtechmod.util.TaggedItemProvider;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;

public enum Nugget implements TaggedItemProvider {
    ALUMINIUM(Ingot.ALUMINIUM.description),
    ANTIMONY(Ingot.ANTIMONY.description),
    BRASS(Ingot.BRASS.description),
    CHROME(Ingot.CHROME.description),
    COPPER("Cu"),
    ELECTRUM(Ingot.ELECTRUM.description),
    INVAR(Ingot.INVAR.description),
    IRIDIUM(Ingot.IRIDIUM.description),
    LEAD(Ingot.LEAD.description),
    NICKEL(Ingot.NICKEL.description),
    OSMIUM(Ingot.OSMIUM.description),
    PLATINUM(Ingot.PLATINUM.description),
    SILVER(Ingot.SILVER.description),
    STEEL(Ingot.STEEL.description),
    TIN("Sn"),
    TITANIUM(Ingot.TITANIUM.description),
    TUNGSTEN(Ingot.TUNGSTEN.description),
    ZINC(Ingot.ZINC.description);

    private final Lazy<Item> instance;
    private final TagKey<Item> tag;

    Nugget(String description) {
        this(new TextComponent(description));
    }

    Nugget(MutableComponent description) {
        this.instance = Lazy.of(() -> new ResourceItem(new ExtendedItemProperties<>().description(description)).registryName(getName(), "nugget"));
        this.tag = GregTechTags.material("nuggets", getName());
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
