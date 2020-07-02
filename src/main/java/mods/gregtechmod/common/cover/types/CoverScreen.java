package mods.gregtechmod.common.cover.types;

import mods.gregtechmod.common.core.GregtechMod;
import mods.gregtechmod.common.cover.ICoverable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class CoverScreen extends CoverGeneric {


    public CoverScreen(ICoverable te, EnumFacing side, ItemStack stack) {
        super(te, side, stack);
    }

    @Override
    public ResourceLocation getIcon() {
        return new ResourceLocation(GregtechMod.MODID, "blocks/machines/adv_machine_screen_random");
    }

    @Override
    public boolean opensGui(EnumFacing side) {
        return side == this.side;
    }
}
