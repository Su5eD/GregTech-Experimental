package dev.su5ed.gregtechmod.cover;

import dev.su5ed.gregtechmod.api.cover.CoverType;
import dev.su5ed.gregtechmod.api.cover.ICoverable;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import static dev.su5ed.gregtechmod.api.util.Reference.location;

public class ScreenCover extends BaseCover {
    public static final ResourceLocation TEXTURE = location("blockentity/adv_machine_screen_random");

    public ScreenCover(ResourceLocation name, ICoverable be, Direction side, Item item) {
        super(name, be, side, item);
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
    public CoverType getType() {
        return CoverType.UTIL;
    }
}
