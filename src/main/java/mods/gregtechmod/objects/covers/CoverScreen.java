package mods.gregtechmod.objects.covers;

import mods.gregtechmod.api.cover.CoverType;
import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.util.Reference;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class CoverScreen extends CoverGeneric {

    public CoverScreen(ResourceLocation name, ICoverable te, EnumFacing side, ItemStack stack) {
        super(name, te, side, stack);
    }

    @Override
    public ResourceLocation getIcon() {
        return new ResourceLocation(Reference.MODID, "blocks/machines/adv_machine_screen_random");
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
