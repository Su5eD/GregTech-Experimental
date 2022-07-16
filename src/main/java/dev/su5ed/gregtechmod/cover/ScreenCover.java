package dev.su5ed.gregtechmod.cover;

import dev.su5ed.gregtechmod.api.cover.CoverCategory;
import dev.su5ed.gregtechmod.api.cover.CoverType;
import dev.su5ed.gregtechmod.api.cover.Coverable;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import static dev.su5ed.gregtechmod.api.util.Reference.location;

public class ScreenCover extends BaseCover {
    public static final ResourceLocation TEXTURE = location("blockentity/adv_machine_screen_random");

    public ScreenCover(CoverType type, Coverable be, Direction side, Item item) {
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

    @Override
    public CoverCategory getCategory() {
        return CoverCategory.UTIL;
    }
}
