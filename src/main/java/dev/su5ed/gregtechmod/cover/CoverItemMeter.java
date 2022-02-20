package dev.su5ed.gregtechmod.cover;

import dev.su5ed.gregtechmod.api.cover.ICoverable;
import dev.su5ed.gregtechmod.util.GtUtil;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public class CoverItemMeter extends CoverMeter {
    public static final ResourceLocation TEXTURE = GtUtil.getCoverTexture("item_meter");

    public CoverItemMeter(ResourceLocation name, ICoverable be, Direction side, ItemStack stack) {
        super(name, be, side, stack);
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
