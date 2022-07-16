package dev.su5ed.gregtechmod.cover;

import dev.su5ed.gregtechmod.api.cover.CoverType;
import dev.su5ed.gregtechmod.api.cover.Coverable;
import dev.su5ed.gregtechmod.util.GtUtil;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public class ItemMeterCover extends MeterCover {
    public static final ResourceLocation TEXTURE = GtUtil.getCoverTexture("item_meter");

    public ItemMeterCover(CoverType type, Coverable be, Direction side, Item item) {
        super(type, be, side, item);
    }

    @Override
    public void doCoverThings() {
        if (this.be instanceof Container) {
            super.doCoverThings();
        }
    }

    @Override
    public int getRedstoneStrength() {
        return ((BlockEntity) this.be).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, this.side)
            .map(ItemHandlerHelper::calcRedstoneFromInventory)
            .orElse(0);
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
