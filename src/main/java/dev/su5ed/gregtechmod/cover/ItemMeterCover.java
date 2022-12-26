package dev.su5ed.gregtechmod.cover;

import dev.su5ed.gregtechmod.api.cover.CoverType;
import dev.su5ed.gregtechmod.api.machine.MachineController;
import dev.su5ed.gregtechmod.util.GtUtil;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.ItemHandlerHelper;

public class ItemMeterCover extends MeterCover {
    public static final ResourceLocation TEXTURE = GtUtil.getCoverTexture("item_meter");

    public ItemMeterCover(CoverType<MachineController> type, MachineController be, Direction side, Item item) {
        super(type, be, side, item);
    }

    @Override
    public void tick() {
        if (this.be instanceof Container) {
            super.tick();
        }
    }

    @Override
    public int getRedstoneStrength() {
        return ((BlockEntity) this.be).getCapability(ForgeCapabilities.ITEM_HANDLER, this.side)
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
