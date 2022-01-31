package mods.gregtechmod.objects.covers;

import mods.gregtechmod.api.cover.CoverType;
import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.util.Reference;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class CoverScreen extends CoverGeneric {
    public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "blocks/machines/adv_machine_screen_random");

    public CoverScreen(ResourceLocation name, ICoverable te, EnumFacing side, ItemStack stack) {
        super(name, te, side, stack);
    }

    @Override
    public ResourceLocation getIcon() {
        return TEXTURE;
    }

    @Override
    public boolean opensGui(EnumFacing side) {
        return side == this.side;
    }

    @Override
    public CoverType getType() {
        return CoverType.UTIL;
    }
}
