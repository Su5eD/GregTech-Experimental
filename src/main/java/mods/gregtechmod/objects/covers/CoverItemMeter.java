package mods.gregtechmod.objects.covers;

import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public class CoverItemMeter extends CoverMeter {
    public static final ResourceLocation TEXTURE = GtUtil.getCoverTexture("item_meter");

    public CoverItemMeter(ResourceLocation name, ICoverable te, EnumFacing side, ItemStack stack) {
        super(name, te, side, stack);
    }

    @Override
    public void doCoverThings() {
        if (te instanceof IInventory) {
            super.doCoverThings();
        }
    }

    @Override
    public int getRedstoneStrength() {
        IItemHandler handler = ((TileEntity)te).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side);
        return ItemHandlerHelper.calcRedstoneFromInventory(handler);
    }

    @Override
    public ResourceLocation getIcon() {
        return TEXTURE;
    }

    @Override
    public boolean letsRedstoneIn() {
        return true;
    }

    @Override
    public boolean letsRedstoneOut() {
        return true;
    }
}
