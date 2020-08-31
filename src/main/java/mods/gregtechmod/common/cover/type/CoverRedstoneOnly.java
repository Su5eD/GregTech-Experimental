package mods.gregtechmod.common.cover.type;

import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.common.core.GregtechMod;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class CoverRedstoneOnly extends CoverGeneric {

    public CoverRedstoneOnly(ICoverable te, EnumFacing side, ItemStack stack) {
        super(te, side, stack);
    }

    @Override
    public ResourceLocation getIcon() {
        return new ResourceLocation(GregtechMod.MODID, "blocks/covers/redstone_only");
    }

    @Override
    public boolean letsRedstoneIn() {
        return true;
    }

    @Override
    public boolean letsRedstoneOut() {
        return true;
    }
}