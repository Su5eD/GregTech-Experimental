package mods.gregtechmod.cover;

import ic2.core.item.ItemMulti;
import ic2.core.item.type.CraftingItemType;
import ic2.core.item.type.PlateResourceType;
import ic2.core.ref.ItemName;
import ic2.core.util.StackUtil;
import mods.gregtechmod.api.cover.ICover;
import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.compat.ModHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class CoverGeneric implements ICover {
    protected final ICoverable te;
    protected final EnumFacing side;
    protected final ItemStack stack;

    public CoverGeneric(ICoverable te, EnumFacing side, ItemStack stack) {
        this.te = te;
        this.side = side;
        this.stack = stack;
    }

    @Override
    public void doCoverThings() {}

    @Override
    public boolean onCoverRightClick(EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
    public boolean onScrewdriverClick(EntityPlayer player) {
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
    public boolean opensGui(EnumFacing side) {
        return side != this.side;
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
    public byte getRedstoneInput() {
        return 0;
    }

    @Override
    public ResourceLocation getIcon() {
        return CoverTexture.valueOf(getCoverName(stack).toUpperCase(Locale.ROOT)).getResourceLocation();
    }

    @Override
    public EnumFacing getSide() {
        return this.side;
    }

    @Override
    public ItemStack getItem() {
        return this.stack;
    }

    @Nonnull
    @Override
    public List<String> getDescription() {
        return Collections.emptyList();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {

    }

    @Override
    public int getTickRate() {
        return 0;
    }

    @Override
    public void onCoverRemoval() {}

    public static boolean isGenericCover(ItemStack stack) {
        Item item = stack.getItem();
        ResourceLocation name = item.getRegistryName();
        String namespace = name.getNamespace();
        String path = name.getPath();

        return namespace.equals(Reference.MODID) && path.startsWith("plate") ||
               namespace.equals("ic2") && !ModHandler.getVariantSafely(ItemName.plate, stack).isEmpty() ||
               StackUtil.checkItemEquality(stack, ItemName.crafting.getItemStack(CraftingItemType.iridium));
    }

    private String getCoverName(ItemStack stack) {
        if (stack.isEmpty()) return "";
        Item item = stack.getItem();
        if (item instanceof ItemMulti) {
            if (StackUtil.checkItemEquality(stack, ItemName.crafting.getItemStack(CraftingItemType.iridium))) return "iridium_alloy";
            return PlateResourceType.values()[stack.getMetadata()].name();  //ic2 plate
        }
        else return item.getRegistryName().getPath().substring(6); //GT plate
    }

    private enum CoverTexture {
        //Generic GregTech
        ALUMINIUM(Reference.MODID, CoverTexture.BLOCK_PATH + "aluminium"),
        BRASS(Reference.MODID, CoverTexture.BLOCK_PATH + "brass"),
        CHROME(Reference.MODID, CoverTexture.BLOCK_PATH + "chrome"),
        ELECTRUM(Reference.MODID, CoverTexture.BLOCK_PATH + "electrum"),
        INVAR(Reference.MODID, CoverTexture.BLOCK_PATH + "invar"),
        IRIDIUM(Reference.MODID, CoverTexture.BLOCK_PATH + "iridium"),
        IRIDIUM_ALLOY(Reference.MODID, CoverTexture.BLOCK_CONNECTED_PATH + "block_iridium_reinforced_tungsten_steel/block_iridium_reinforced_tungsten_steel0c"),
        LEAD(Reference.MODID, CoverTexture.BLOCK_PATH + "lead"),
        NICKEL(Reference.MODID, CoverTexture.BLOCK_PATH + "nickel"),
        OSMIUM(Reference.MODID, CoverTexture.BLOCK_PATH + "osmium"),
        PLATINUM(Reference.MODID, CoverTexture.BLOCK_PATH + "platinum"),
        SILVER(Reference.MODID, CoverTexture.BLOCK_PATH + "silver"),
        STEEL(Reference.MODID, CoverTexture.BLOCK_PATH + "steel"),
        TITANIUM(Reference.MODID, CoverTexture.BLOCK_PATH + "titanium"),
        TUNGSTEN(Reference.MODID, CoverTexture.BLOCK_PATH + "tungsten"),
        TUNGSTEN_STEEL(Reference.MODID, CoverTexture.BLOCK_CONNECTED_PATH + "block_tungsten_steel/block_tungsten_steel0c"),
        ZINC(Reference.MODID, CoverTexture.BLOCK_PATH + "zinc"),
        //Generic IC2
        COPPER("ic2", CoverTexture.IC2_BLOCK_PATH + "bronze_block"),
        BRONZE("ic2", CoverTexture.IC2_BLOCK_PATH + "bronze_block"),
        TIN("ic2", CoverTexture.IC2_BLOCK_PATH + "tin_block"),
        REFINED_IRON("ic2", CoverTexture.IC2_BLOCK_PATH + "machine"),
        //Generic Minecraft
        IRON("minecraft", "blocks/iron_block"),
        GOLD("minecraft", "blocks/gold_block"),
        LAPIS("minecraft", "blocks/lapis_block"),
        OBSIDIAN("minecraft", "blocks/obsidian"),
        WOOD("minecraft", "blocks/planks_oak");
        private final String domain;
        private final String path;
        private static final String BLOCK_PATH = "blocks/block_";
        private static final String IC2_BLOCK_PATH = "blocks/resource/";
        private static final String BLOCK_CONNECTED_PATH = "blocks/connected/";

        CoverTexture(String domain, String path) {
            this.domain = domain;
            this.path = path;
        }

        public ResourceLocation getResourceLocation() {
            return new ResourceLocation(domain, path);
        }
    }
}
