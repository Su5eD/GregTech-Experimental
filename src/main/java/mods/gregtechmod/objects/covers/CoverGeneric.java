package mods.gregtechmod.objects.covers;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import ic2.core.item.type.CraftingItemType;
import ic2.core.item.type.PlateResourceType;
import ic2.core.ref.ItemName;
import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.util.IItemProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;

import java.util.Collection;
import java.util.Locale;

public class CoverGeneric extends CoverBase {
    private static final Multimap<CoverTexture, ItemStack> PLATE_ITEMS = HashMultimap.create();

    static {
        StreamEx.of(BlockItems.Plate.values())
            .without(BlockItems.Plate.BATTERY_ALLOY, BlockItems.Plate.MAGNALIUM, BlockItems.Plate.SILICON)
            .mapToEntry(plate -> plate.name().toLowerCase(Locale.ROOT), IItemProvider::getItemStack)
            .append(StreamEx.of(PlateResourceType.bronze, PlateResourceType.copper, PlateResourceType.gold, PlateResourceType.iron,
                    PlateResourceType.lapis, PlateResourceType.lead, PlateResourceType.obsidian, PlateResourceType.steel, PlateResourceType.tin)
                .mapToEntry(plate -> plate.getName().toLowerCase(Locale.ROOT), ItemName.plate::getItemStack))
            .append("iridium_alloy", ItemName.crafting.getItemStack(CraftingItemType.iridium))
            .mapKeys(s -> CoverTexture.valueOf(s.toUpperCase(Locale.ROOT)))
            .forKeyValue(PLATE_ITEMS::put);
    }

    private final ResourceLocation coverTexture;

    public CoverGeneric(ResourceLocation name, ICoverable te, EnumFacing side, ItemStack stack) {
        super(name, te, side, stack);

        this.coverTexture = EntryStream.of(PLATE_ITEMS.asMap())
            .flatMapValues(Collection::stream)
            .filterValues(stack::isItemEqual)
            .keys()
            .findFirst()
            .map(CoverTexture::getResourceLocation)
            .orElseThrow(() -> new IllegalArgumentException("Invalid generic cover item supplied (" + stack + ")"));
    }

    public static boolean isGenericCover(ItemStack stack) {
        return PLATE_ITEMS.values().stream().anyMatch(stack::isItemEqual);
    }

    @Override
    public ResourceLocation getIcon() {
        return this.coverTexture;
    }

    private enum CoverTexture {
        //Generic GregTech
        ALUMINIUM(Reference.MODID, CoverTexture.BLOCK_PATH + "aluminium"),
        BRASS(Reference.MODID, CoverTexture.BLOCK_PATH + "brass"),
        CHROME(Reference.MODID, CoverTexture.BLOCK_PATH + "chrome"),
        ELECTRUM(Reference.MODID, CoverTexture.BLOCK_PATH + "electrum"),
        INVAR(Reference.MODID, CoverTexture.BLOCK_PATH + "invar"),
        IRIDIUM(Reference.MODID, CoverTexture.BLOCK_PATH + "iridium"),
        IRIDIUM_ALLOY(Reference.MODID, CoverTexture.BLOCK_CONNECTED_PATH + "iridium_reinforced_tungsten_steel/iridium_reinforced_tungsten_steel"),
        LEAD(Reference.MODID, CoverTexture.BLOCK_PATH + "lead"),
        NICKEL(Reference.MODID, CoverTexture.BLOCK_PATH + "nickel"),
        OSMIUM(Reference.MODID, CoverTexture.BLOCK_PATH + "osmium"),
        PLATINUM(Reference.MODID, CoverTexture.BLOCK_PATH + "platinum"),
        SILVER(Reference.MODID, CoverTexture.BLOCK_PATH + "silver"),
        STEEL(Reference.MODID, CoverTexture.BLOCK_PATH + "steel"),
        TITANIUM(Reference.MODID, CoverTexture.BLOCK_PATH + "titanium"),
        TUNGSTEN(Reference.MODID, CoverTexture.BLOCK_PATH + "tungsten"),
        TUNGSTEN_STEEL(Reference.MODID, CoverTexture.BLOCK_CONNECTED_PATH + "tungsten_steel/tungsten_steel"),
        ZINC(Reference.MODID, CoverTexture.BLOCK_PATH + "zinc"),
        //Generic IC2
        COPPER("ic2", CoverTexture.IC2_BLOCK_PATH + "copper_block"),
        BRONZE("ic2", CoverTexture.IC2_BLOCK_PATH + "bronze_block"),
        TIN("ic2", CoverTexture.IC2_BLOCK_PATH + "tin_block"),
        REFINED_IRON("ic2", CoverTexture.IC2_BLOCK_PATH + "machine"),
        //Generic Minecraft
        IRON("minecraft", "blocks/iron_block"),
        GOLD("minecraft", "blocks/gold_block"),
        LAPIS("minecraft", "blocks/lapis_block"),
        OBSIDIAN("minecraft", "blocks/obsidian"),
        WOOD("minecraft", "blocks/planks_oak");

        private final String namespace;
        private final String path;
        private static final String BLOCK_PATH = "blocks/";
        private static final String IC2_BLOCK_PATH = "blocks/resource/";
        private static final String BLOCK_CONNECTED_PATH = "blocks/connected/";

        CoverTexture(String namespace, String path) {
            this.namespace = namespace;
            this.path = path;
        }

        public ResourceLocation getResourceLocation() {
            return new ResourceLocation(this.namespace, this.path);
        }
    }
}
