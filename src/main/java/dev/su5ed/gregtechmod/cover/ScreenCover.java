package dev.su5ed.gregtechmod.cover;

import dev.su5ed.gregtechmod.api.cover.CoverType;
import dev.su5ed.gregtechmod.api.cover.ICoverable;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import static dev.su5ed.gregtechmod.api.util.Reference.location;

public class ScreenCover extends GenericCover {
    public static final ResourceLocation TEXTURE = location("blockentity/adv_machine_screen_random");

    public ScreenCover(ResourceLocation name, ICoverable be, Direction side, ItemStack stack) {
        super(name, be, side, stack);
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
