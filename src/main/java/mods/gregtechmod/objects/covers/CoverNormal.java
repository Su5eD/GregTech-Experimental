package mods.gregtechmod.objects.covers;

import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class CoverNormal extends CoverGeneric {
    public static final ResourceLocation TEXTURE_NORMAL = GtUtil.getCoverTexture("normal");
    public static final ResourceLocation TEXTURE_NOREDSTONE = GtUtil.getCoverTexture("noredstone");
    
    @NBTPersistent
    protected CoverMeter.MeterMode mode = CoverMeter.MeterMode.NORMAL;

    public CoverNormal(ResourceLocation name, ICoverable te, EnumFacing side, ItemStack stack) {
        super(name, te, side, stack);
    }

    @Override
    public ResourceLocation getIcon() {
        return mode == CoverMeter.MeterMode.NORMAL ? TEXTURE_NORMAL : TEXTURE_NOREDSTONE;
    }

    @Override
    public boolean onScrewdriverClick(EntityPlayer player) {
        mode = mode.next();
        te.updateRender();
        return true;
    }

    @Override
    public boolean letsRedstoneIn() {
        return mode == CoverMeter.MeterMode.NORMAL;
    }

    @Override
    public boolean letsRedstoneOut() {
        return mode == CoverMeter.MeterMode.NORMAL;
    }

    @Override
    public boolean allowEnergyTransfer() {
        return mode == CoverMeter.MeterMode.INVERTED;
    }

    @Override
    public boolean letsLiquidsIn() {
        return mode == CoverMeter.MeterMode.INVERTED;
    }

    @Override
    public boolean letsLiquidsOut() {
        return mode == CoverMeter.MeterMode.INVERTED;
    }

    @Override
    public boolean letsItemsIn() {
        return mode == CoverMeter.MeterMode.INVERTED;
    }

    @Override
    public boolean letsItemsOut() {
        return mode == CoverMeter.MeterMode.INVERTED;
    }
}
