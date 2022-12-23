package dev.su5ed.gregtechmod.cover;

import dev.su5ed.gregtechmod.api.cover.CoverType;
import dev.su5ed.gregtechmod.util.GtUtil;
import dev.su5ed.gregtechmod.util.InvUtil;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ConveyorCover extends InventoryCover {
    public static final ResourceLocation TEXTURE = GtUtil.getCoverTexture("conveyor");

    public ConveyorCover(CoverType<BlockEntity> type, BlockEntity be, Direction side, Item item) {
        super(type, be, side, item);
    }

    @Override
    public void tick() {
        if (shouldUseEnergy(128)) {
            if (this.energyHandler.canUseEnergy(128)) {
                this.energyHandler.useEnergy(moveItemStack(this.be, this.side, this.mode));
            }
        }
        else {
            moveItemStack(this.be, side, mode);   
        }
    }

    public static int moveItemStack(BlockEntity source, Direction side, InventoryMode mode) {
        BlockEntity target = source.getLevel().getBlockEntity(source.getBlockPos().relative(side));
        return InvUtil.moveItemStack(mode.isImport ? target : source, mode.isImport ? source : target, mode.isImport ? side.getOpposite() : side, mode.isImport ? side : side.getOpposite(), 1, 64);
    }

    @Override
    public ResourceLocation getIcon() {
        return TEXTURE;
    }

    @Override
    public boolean letsLiquidsIn() {
        return true;
    }

    @Override
    public boolean letsLiquidsOut() {
        return true;
    }

    @Override
    public int getTickRate() {
        return 10;
    }
}
