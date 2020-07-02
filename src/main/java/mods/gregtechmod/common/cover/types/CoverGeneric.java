package mods.gregtechmod.common.cover.types;

import ic2.core.IC2;
import ic2.core.item.ItemMulti;
import ic2.core.item.type.PlateResourceType;
import mods.gregtechmod.common.core.GregtechMod;
import mods.gregtechmod.common.cover.ICover;
import mods.gregtechmod.common.cover.ICoverable;
import mods.gregtechmod.common.objects.items.base.ItemBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;

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
    public ResourceLocation getIcon() {
        return CoverTextures.valueOf(getCoverName(stack)).getResourceLocation();
    }

    @Override
    public EnumFacing getSide() {
        return this.side;
    }

    @Override
    public ItemStack getItem() {
        return this.stack;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {

    }

    @Override
    public short getTickRate() {
        return 0;
    }

    @Override
    public void onCoverRemoved() {}

    public static boolean isGenericCover(ItemStack stack) {
        //TODO: replace item translatetion key with stack key
        Item item = stack.getItem();
        System.out.println("item key: "+item.getTranslationKey());
        System.out.println("stack key: "+stack.getTranslationKey());
        String key = item.getTranslationKey();
        if (item instanceof ItemBase && key.startsWith("item.plate")) return true;
        else if (item instanceof ItemMulti && key.substring(IC2.MODID.length() + 1).equals("plate")) return true;
        else return item instanceof ItemMulti && key.substring(IC2.MODID.length() + 1).equals("crafting") && stack.getMetadata() == 4; //iridium plate, rarest cover
    }

    private String getCoverName(ItemStack stack) {
        if (stack.getItem() instanceof ItemMulti) {
            if (stack.getTranslationKey().equals("ic2.crafting.iridium")) return "iridium";
            return PlateResourceType.values()[stack.getMetadata()].name();  //ic2 plate
        }
        else return stack.getTranslationKey().substring(11); //GT plate
    }

    public enum CoverTextures {
        //Generic GregTech
        aluminium(GregtechMod.MODID, CoverTextures.blockPath+"aluminium"),
        brass(GregtechMod.MODID, CoverTextures.blockPath+"brass"),
        chrome(GregtechMod.MODID, CoverTextures.blockPath+"chrome"),
        electrum(GregtechMod.MODID, CoverTextures.blockPath+"electrum"),
        invar(GregtechMod.MODID, CoverTextures.blockPath+"invar"),
        iridium(GregtechMod.MODID, CoverTextures.blockConnectedPath+"block_iridium_reinforced_tungstensteel/block_iridium_reinforced_tungstensteel0c"),
        nickel(GregtechMod.MODID, CoverTextures.blockPath+"nickel"),
        osmium(GregtechMod.MODID, CoverTextures.blockPath+"osmium"),
        platinum(GregtechMod.MODID, CoverTextures.blockPath+"platinum"),
        titanium(GregtechMod.MODID, CoverTextures.blockPath+"titanium"),
        tungsten(GregtechMod.MODID, CoverTextures.blockPath+"tungsten"),
        tungstensteel(GregtechMod.MODID, CoverTextures.blockConnectedPath+"block_tungstensteel/block_tungstensteel0c"),
        zinc(GregtechMod.MODID, CoverTextures.blockPath+"zinc"),
        //Generic Minecraft
        iron("minecraft", "blocks/iron_block"),
        gold("minecraft", "blocks/gold_block"),
        lapis("minecraft", "blocks/lapis_block"),
        obsidian("minecraft", "blocks/obsidian"),
        wood("minecraft", "blocks/planks_oak"),
        //Generic IC2
        copper("ic2", CoverTextures.ic2BlockPath+"bronze_block"),
        silver("ic2", CoverTextures.ic2BlockPath+"silver_block"),
        bronze("ic2", CoverTextures.ic2BlockPath+"bronze_block"),
        tin("ic2", CoverTextures.ic2BlockPath+"tin_block"),
        lead("ic2", CoverTextures.ic2BlockPath+"lead_block"),
        steel("ic2", CoverTextures.ic2BlockPath+"steel_block");
        private final String domain;
        private final String path;
        private static final String blockPath = "blocks/block_";
        private static final String ic2BlockPath = "blocks/resource/";
        private static final String blockConnectedPath = "blocks/connected/";

        CoverTextures(String domain, String path) {
            this.domain = domain;
            this.path = path;
        }

        public ResourceLocation getResourceLocation() {
            return new ResourceLocation(domain, path);
        }
    }
}
