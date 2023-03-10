package dev.su5ed.gtexperimental.cover;

import dev.su5ed.gtexperimental.api.cover.CoverType;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;

import static dev.su5ed.gtexperimental.util.GtUtil.location;

public class ScreenCover extends BaseCover {
    public static final ResourceLocation TEXTURE = location("blockentity/adv_machine_screen_random");

    public ScreenCover(CoverType type, BlockEntity be, Direction side, Item item) {
        super(type, be, side, item);
    }

    @Override
    public ResourceLocation getIcon() {
        return TEXTURE;
    }

    @Override
    public boolean opensGui(Direction side) {
        return side == this.side;
    }
}
