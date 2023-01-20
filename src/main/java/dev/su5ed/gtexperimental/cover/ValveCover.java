package dev.su5ed.gtexperimental.cover;

import dev.su5ed.gtexperimental.api.cover.CoverType;
import dev.su5ed.gtexperimental.util.GtUtil;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ValveCover extends PumpCover {
    public static final ResourceLocation TEXTURE = GtUtil.getCoverTexture("valve");

    public ValveCover(CoverType<BlockEntity> type, BlockEntity be, Direction side, Item item) {
        super(type, be, side, item);
    }

    @Override
    public void tick() {
        if (shouldUseEnergy(128) && this.energyHandler.canUseEnergy(128)) {
            this.energyHandler.useEnergy(ConveyorCover.moveItemStack(this.be, this.side, this.mode));
        }
        
        super.tick();
    }

    @Override
    public ResourceLocation getIcon() {
        return TEXTURE;
    }

    @Override
    public boolean letsItemsIn() {
        return this.mode.allowsInput();
    }

    @Override
    public boolean letsItemsOut() {
        return this.mode.allowsOutput();
    }

    @Override
    public int getTickRate() {
        return 2;
    }
}
