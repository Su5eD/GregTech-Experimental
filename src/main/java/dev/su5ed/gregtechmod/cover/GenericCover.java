package dev.su5ed.gregtechmod.cover;

import dev.su5ed.gregtechmod.GregTechTags;
import dev.su5ed.gregtechmod.api.cover.CoverType;
import dev.su5ed.gregtechmod.api.Reference;
import dev.su5ed.gregtechmod.compat.ModHandler;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import one.util.streamex.StreamEx;

import java.util.Locale;

public class GenericCover extends BaseCover<BlockEntity> {
    private final CoverTexture texture;

    public GenericCover(CoverType<BlockEntity> type, BlockEntity be, Direction side, Item item) {
        super(type, be, side, item);

        //noinspection deprecation
        this.texture = StreamEx.of(CoverTexture.values())
            .findFirst(ct -> item.builtInRegistryHolder().is(ct.tag))
            .orElseThrow();
    }

    @Override
    public ResourceLocation getIcon() {
        return this.texture.getLocation();
    }

    public static boolean isGenericCover(ItemStack stack) {
        return StreamEx.of(CoverTexture.values())
            .anyMatch(ct -> stack.is(ct.tag));
    }

    private enum CoverTexture {
        // GregTech
        ALUMINIUM(Reference.MODID, CoverTexture.BLOCK_PATH, "aluminium"),
        BRASS(Reference.MODID, CoverTexture.BLOCK_PATH, "brass"),
        CHROME(Reference.MODID, CoverTexture.BLOCK_PATH, "chrome"),
        ELECTRUM(Reference.MODID, CoverTexture.BLOCK_PATH, "electrum"),
        INVAR(Reference.MODID, CoverTexture.BLOCK_PATH, "invar"),
        IRIDIUM(Reference.MODID, CoverTexture.BLOCK_PATH, "iridium"),
        IRIDIUM_ALLOY(GregTechTags.IRIDIUM_ALLOY, Reference.MODID, CoverTexture.BLOCK_CONNECTED_PATH, "iridium_reinforced_tungsten_steel", "iridium_reinforced_tungsten_steel"),
        LEAD(Reference.MODID, CoverTexture.BLOCK_PATH, "lead"),
        NICKEL(Reference.MODID, CoverTexture.BLOCK_PATH, "nickel"),
        OSMIUM(Reference.MODID, CoverTexture.BLOCK_PATH, "osmium"),
        PLATINUM(Reference.MODID, CoverTexture.BLOCK_PATH, "platinum"),
        SILVER(Reference.MODID, CoverTexture.BLOCK_PATH, "silver"),
        STEEL(Reference.MODID, CoverTexture.BLOCK_PATH, "steel"),
        TITANIUM(Reference.MODID, CoverTexture.BLOCK_PATH, "titanium"),
        TUNGSTEN(Reference.MODID, CoverTexture.BLOCK_PATH, "tungsten"),
        TUNGSTEN_STEEL(Reference.MODID, CoverTexture.BLOCK_CONNECTED_PATH, "tungsten_steel", "tungsten_steel"),
        ZINC(Reference.MODID, CoverTexture.BLOCK_PATH, "zinc"),
        // IC2
        BRONZE(ModHandler.IC2_MODID, CoverTexture.IC2_BLOCK_PATH, "bronze_block"),
        TIN(ModHandler.IC2_MODID, CoverTexture.IC2_BLOCK_PATH, "tin_block"),
        REFINED_IRON(ModHandler.IC2_MODID, CoverTexture.IC2_BLOCK_PATH, "machine"),
        // Minecraft
        COPPER("minecraft", CoverTexture.BLOCK_PATH, "copper_block"),
        IRON("minecraft", CoverTexture.BLOCK_PATH, "iron_block"),
        GOLD("minecraft", CoverTexture.BLOCK_PATH, "gold_block"),
        LAPIS("minecraft", CoverTexture.BLOCK_PATH, "lapis_block"),
        OBSIDIAN("minecraft", CoverTexture.BLOCK_PATH, "obsidian"),
        WOOD("minecraft", CoverTexture.BLOCK_PATH, "oak_planks");

        private static final String BLOCK_PATH = "block";
        private static final String IC2_BLOCK_PATH = "blocks/resource";
        private static final String BLOCK_CONNECTED_PATH = "block/connected";

        private final ResourceLocation location;
        private final TagKey<Item> tag;

        CoverTexture(String domain, String... path) {
            this.location = new ResourceLocation(domain, String.join("/", path));
            this.tag = GregTechTags.material("plates", name().toLowerCase(Locale.ROOT));
        }

        CoverTexture(TagKey<Item> tag, String domain, String... path) {
            this.location = new ResourceLocation(domain, String.join("/", path));
            this.tag = tag;
        }

        public ResourceLocation getLocation() {
            return this.location;
        }
    }
}
