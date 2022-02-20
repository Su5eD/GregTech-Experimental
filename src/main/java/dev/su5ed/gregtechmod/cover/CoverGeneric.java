package dev.su5ed.gregtechmod.cover;

import dev.su5ed.gregtechmod.api.cover.CoverType;
import dev.su5ed.gregtechmod.api.cover.ICover;
import dev.su5ed.gregtechmod.api.cover.ICoverable;
import dev.su5ed.gregtechmod.api.util.Reference;
import dev.su5ed.gregtechmod.object.Plate;
import dev.su5ed.gregtechmod.util.nbt.NBTSaveHandler;
import ic2.core.IC2;
import ic2.core.ref.Ic2Items;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import one.util.streamex.StreamEx;

import java.util.Collection;
import java.util.Locale;
import java.util.Set;

public class CoverGeneric implements ICover {
    private static final Collection<Item> IC2_PLATES = Set.of(
        Ic2Items.BRONZE_PLATE, Ic2Items.COPPER_PLATE, Ic2Items.GOLD_PLATE, Ic2Items.TIN_PLATE,
        Ic2Items.IRON_PLATE, Ic2Items.LAPIS_PLATE, Ic2Items.OBSIDIAN_PLATE, Ic2Items.IRIDIUM
    );
    private static final Collection<Item> GT_PLATES = StreamEx.of(Plate.values())
        .map(Plate::getItem)
        .toImmutableSet();
    
    private final ResourceLocation name;
    protected final ICoverable be;
    protected final Direction side;
    protected final ItemStack stack;

    public CoverGeneric(ResourceLocation name, ICoverable be, Direction side, ItemStack stack) {
        this.name = name;
        this.be = be;
        this.side = side;
        this.stack = stack;
    }

    @Override
    public ResourceLocation getName() {
        return name;
    }

    @Override
    public void doCoverThings() {}

    @Override
    public boolean onCoverRightClick(Player player, InteractionHand hand, Direction side, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
    public boolean onScrewdriverClick(Player player) {
        return false;
    }

    @Override
    public boolean allowEnergyTransfer() {
        return false;
    }

    @Override
    public boolean letsRedstoneIn() {
        return false;
    }

    @Override
    public boolean letsRedstoneOut() {
        return false;
    }

    @Override
    public boolean letsLiquidsIn() {
        return false;
    }

    @Override
    public boolean letsLiquidsOut() {
        return false;
    }

    @Override
    public boolean letsItemsIn() {
        return false;
    }

    @Override
    public boolean letsItemsOut() {
        return false;
    }

    @Override
    public boolean opensGui(Direction side) {
        return false;
    }

    @Override
    public boolean acceptsRedstone() {
        return false;
    }

    @Override
    public boolean overrideRedstoneOut() {
        return false;
    }

    @Override
    public int getRedstoneInput() {
        return 0;
    }

    @Override
    public ResourceLocation getIcon() {
        return CoverTexture.valueOf(getCoverName(this.stack).toUpperCase(Locale.ROOT)).getTextureLocation();
    }

    @Override
    public Direction getSide() {
        return this.side;
    }

    @Override
    public ItemStack getItem() {
        return this.stack;
    }
    
    @Override
    public CoverType getType() {
        return CoverType.GENERIC;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        NBTSaveHandler.writeClassToNBT(this, tag);
        return tag;
    }

    @Override
    public void load(CompoundTag tag) {
        NBTSaveHandler.readClassFromNBT(this, tag);
    }

    @Override
    public int getTickRate() {
        return 0;
    }

    @Override
    public void onCoverRemove() {}

    public static boolean isGenericCover(ItemStack stack) {
        Item item = stack.getItem();
        
        return GT_PLATES.contains(item) || IC2_PLATES.contains(item);
    }

    private String getCoverName(ItemStack stack) {
        if (!stack.isEmpty()) {
            Item item = stack.getItem();
            return item == Ic2Items.IRIDIUM ? "iridium_alloy" : item.getRegistryName().getPath().replace("_plate", "");
        }
        return "";
    }

    private enum CoverTexture {
        //GregTech
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
        //IC2
        COPPER(IC2.MODID, CoverTexture.IC2_BLOCK_PATH + "copper_block"),
        BRONZE(IC2.MODID, CoverTexture.IC2_BLOCK_PATH + "bronze_block"),
        TIN(IC2.MODID, CoverTexture.IC2_BLOCK_PATH + "tin_block"),
        REFINED_IRON(IC2.MODID, CoverTexture.IC2_BLOCK_PATH + "machine"),
        //Minecraft
        IRON("minecraft", "block/iron_block"),
        GOLD("minecraft", "block/gold_block"),
        LAPIS("minecraft", "block/lapis_block"),
        OBSIDIAN("minecraft", "block/obsidian"),
        WOOD("minecraft", "block/oak_planks");
        private final ResourceLocation location;
        private static final String BLOCK_PATH = "block/";
        private static final String IC2_BLOCK_PATH = "blocks/resource/";
        private static final String BLOCK_CONNECTED_PATH = "block/connected/";

        CoverTexture(String domain, String path) {
            this.location = new ResourceLocation(domain, path);
        }

        public ResourceLocation getTextureLocation() {
            return this.location;
        }
    }
}
