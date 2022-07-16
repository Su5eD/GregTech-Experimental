package dev.su5ed.gregtechmod.cover;

import dev.su5ed.gregtechmod.api.cover.CoverCategory;
import dev.su5ed.gregtechmod.api.cover.CoverType;
import dev.su5ed.gregtechmod.api.cover.Coverable;
import dev.su5ed.gregtechmod.util.GtUtil;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class RedstoneOnlyCover extends BaseCover {
    public static final ResourceLocation TEXTURE = GtUtil.getCoverTexture("redstone_only");

    public RedstoneOnlyCover(CoverType type, Coverable te, Direction side, Item item) {
        super(type, te, side, item);
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

    @Override
    public CoverCategory getCategory() {
        return CoverCategory.OTHER;
    }
}
