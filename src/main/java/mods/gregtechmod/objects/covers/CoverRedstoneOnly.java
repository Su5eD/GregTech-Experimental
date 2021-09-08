package mods.gregtechmod.objects.covers;

import mods.gregtechmod.api.cover.CoverType;
import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.util.Reference;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class CoverRedstoneOnly extends CoverGeneric {

    public CoverRedstoneOnly(ResourceLocation name, ICoverable te, EnumFacing side, ItemStack stack) {
        super(name, te, side, stack);
    }

    @Override
    public ResourceLocation getIcon() {
        return new ResourceLocation(Reference.MODID, "blocks/covers/redstone_only");
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
    public CoverType getType() {
        return CoverType.OTHER;
    }
}
