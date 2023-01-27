package dev.su5ed.gtexperimental.object;

import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.compat.ModHandler;
import dev.su5ed.gtexperimental.item.MortarItem;
import dev.su5ed.gtexperimental.item.ResourceItem;
import dev.su5ed.gtexperimental.item.ResourceItem.ExtendedItemProperties;
import dev.su5ed.gtexperimental.item.SolderingMetalItem;
import dev.su5ed.gtexperimental.util.GtLocale;
import dev.su5ed.gtexperimental.util.TaggedItemProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public enum Miscellaneous implements TaggedItemProvider {
    COPPER_CREDIT(GtLocale.translateItemDescription("credit", 0.125)),
    DIAMOND_CREDIT(GtLocale.translateItemDescription("credit", 512)),
    EMPTY_SPRAY_CAN(() -> new ResourceItem(new ExtendedItemProperties<>().autoDescription()), GregTechTags.CRAFTING_SPRAY_CAN),
    FLINT_MORTAR(GtLocale.translateItemDescription("mortar")),
    FLOUR(GregTechTags.dust("wheat")),
    GOLD_CREDIT(GtLocale.translateItemDescription("credit", 64)),
    GREEN_SAPPHIRE(Dust.GREEN_SAPPHIRE.description, GregTechTags.material("gems", "green_sapphire")),
    GREG_COIN(() -> new ResourceItem(new ExtendedItemProperties<>().autoDescription())),
    INDIGO_BLOSSOM,
    INDIGO_DYE(Tags.Items.DYES_BLUE),
    IRON_MORTAR(() -> new MortarItem(63, 1, () -> new ItemStack(ModHandler.getModItem("iron_dust")))),
    LAZURITE_CHUNK(Component.literal("(Al6Si6Ca8Na8)8"), GregTechTags.LAZURITE_CHUNK),
    OLIVINE(Dust.OLIVINE.description, GregTechTags.material("gems", "olivine")),
    OIL_BERRY,
    RAW_BAUXITE,
    RAW_CASSITERITE,
    RAW_GALENA,
    RAW_SHELDONITE,
    RAW_TETRAHEDRITE,
    RAW_TUNGSTATE,
    RED_GARNET(Dust.RED_GARNET.description, GregTechTags.material("gems", "red_garnet")),
    RUBY(Dust.RUBY.description, GregTechTags.material("gems", "ruby")),
    SAPPHIRE(Dust.SAPPHIRE.description, GregTechTags.material("gems", "sapphire")),
    SILVER_CREDIT(GtLocale.translateItemDescription("credit", 8)),
    SOLDERING_LEAD(() -> new SolderingMetalItem(10)),
    SOLDERING_TIN(() -> new SolderingMetalItem(50)),
    YELLOW_GARNET(Dust.YELLOW_GARNET.description, GregTechTags.material("gems", "yellow_garnet"));

    private final Lazy<Item> instance;
    private final TagKey<Item> tag;
    
    Miscellaneous() {
        this((TagKey<Item>) null);
    }

    Miscellaneous(TagKey<Item> tag) {
        this(() -> new ResourceItem(new ExtendedItemProperties<>()), tag);
    }
    
    Miscellaneous(MutableComponent description) {
        this(() -> new ResourceItem(new ExtendedItemProperties<>().description(description)), null);
    }

    Miscellaneous(MutableComponent description, TagKey<Item> tag) {
        this(() -> new ResourceItem(new ExtendedItemProperties<>().description(description)), tag);
    }

    Miscellaneous(Supplier<Item> supplier) {
        this(supplier, null);
    }

    Miscellaneous(Supplier<Item> supplier, TagKey<Item> tag) {
        this.instance = Lazy.of(supplier);
        this.tag = tag;
    }

    @Override
    public String getRegistryName() {
        return getName();
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
