package dev.su5ed.gregtechmod.cover;

import dev.su5ed.gregtechmod.api.cover.ICoverable;
import dev.su5ed.gregtechmod.util.GtUtil;
import dev.su5ed.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class CoverNormal extends CoverGeneric {
    public static final ResourceLocation TEXTURE_NORMAL = GtUtil.getCoverTexture("normal");
    public static final ResourceLocation TEXTURE_NOREDSTONE = GtUtil.getCoverTexture("noredstone");

    @NBTPersistent
    protected CoverMeter.MeterMode mode = CoverMeter.MeterMode.NORMAL;

    public CoverNormal(ResourceLocation name, ICoverable be, Direction side, ItemStack stack) {
        super(name, be, side, stack);
    }

    @Override
    public ResourceLocation getIcon() {
        return mode == CoverMeter.MeterMode.NORMAL ? TEXTURE_NORMAL : TEXTURE_NOREDSTONE;
    }

    @Override
    public boolean onScrewdriverClick(Player player) {
        this.mode = this.mode.next();
        this.be.updateRender();
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
