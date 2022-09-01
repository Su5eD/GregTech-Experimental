package dev.su5ed.gregtechmod.cover;

import dev.su5ed.gregtechmod.api.cover.CoverType;
import dev.su5ed.gregtechmod.api.util.Reference;
import dev.su5ed.gregtechmod.compat.ModHandler;
import dev.su5ed.gregtechmod.object.Plate;
import ic2.core.ref.Ic2Items;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import one.util.streamex.StreamEx;

import java.util.Collection;
import java.util.Locale;
import java.util.Set;

public class GenericCover extends BaseCover<BlockEntity> {
    // TODO Use tags for compat
    private static final Collection<Item> IC2_PLATES = Set.of(
        Ic2Items.BRONZE_PLATE, Ic2Items.COPPER_PLATE, Ic2Items.GOLD_PLATE, Ic2Items.TIN_PLATE,
        Ic2Items.IRON_PLATE, Ic2Items.LAPIS_PLATE, Ic2Items.OBSIDIAN_PLATE, Ic2Items.IRIDIUM
    );
    private static final Collection<Item> GT_PLATES = StreamEx.of(Plate.values())
        .map(Plate::getItem)
        .toImmutableSet();
    
    private final CoverTexture texture;

    public GenericCover(CoverType<BlockEntity> type, BlockEntity be, Direction side, Item item) {
        super(type, be, side, item);
        
        String coverName = item.getRegistryName().getPath().replace("_plate", "");
        this.texture = CoverTexture.valueOf(coverName.toUpperCase(Locale.ROOT));
    }

    @Override
    public ResourceLocation getIcon() {
        return this.texture.getLocation();
    }

    public static boolean isGenericCover(ItemStack stack) {
        Item item = stack.getItem();

        return GT_PLATES.contains(item) || IC2_PLATES.contains(item);
    }

    private enum CoverTexture {
        //GregTech
        ALUMINIUM(Reference.MODID, CoverTexture.BLOCK_PATH, "aluminium"),
        BRASS(Reference.MODID, CoverTexture.BLOCK_PATH, "brass"),
        CHROME(Reference.MODID, CoverTexture.BLOCK_PATH, "chrome"),
        ELECTRUM(Reference.MODID, CoverTexture.BLOCK_PATH, "electrum"),
        INVAR(Reference.MODID, CoverTexture.BLOCK_PATH, "invar"),
        IRIDIUM(Reference.MODID, CoverTexture.BLOCK_PATH, "iridium"),
        IRIDIUM_ALLOY(Reference.MODID, CoverTexture.BLOCK_CONNECTED_PATH, "iridium_reinforced_tungsten_steel", "iridium_reinforced_tungsten_steel"),
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
        //IC2
        COPPER(ModHandler.IC2_MODID, CoverTexture.IC2_BLOCK_PATH, "copper_block"),
        BRONZE(ModHandler.IC2_MODID, CoverTexture.IC2_BLOCK_PATH, "bronze_block"),
        TIN(ModHandler.IC2_MODID, CoverTexture.IC2_BLOCK_PATH, "tin_block"),
        REFINED_IRON(ModHandler.IC2_MODID, CoverTexture.IC2_BLOCK_PATH, "machine"),
        //Minecraft
        IRON("minecraft", CoverTexture.BLOCK_PATH, "iron_block"),
        GOLD("minecraft", CoverTexture.BLOCK_PATH, "gold_block"),
        LAPIS("minecraft", CoverTexture.BLOCK_PATH, "lapis_block"),
        OBSIDIAN("minecraft", CoverTexture.BLOCK_PATH, "obsidian"),
        WOOD("minecraft", CoverTexture.BLOCK_PATH, "oak_planks");

        private static final String BLOCK_PATH = "block";
        private static final String IC2_BLOCK_PATH = "blocks/resource";
        private static final String BLOCK_CONNECTED_PATH = "block/connected";

        private final ResourceLocation location;

        CoverTexture(String domain, String... path) {
            this.location = new ResourceLocation(domain, String.join("/", path));
        }

        public ResourceLocation getLocation() {
            return this.location;
        }
    }
}
