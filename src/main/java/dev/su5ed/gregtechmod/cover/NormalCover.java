package dev.su5ed.gregtechmod.cover;

import dev.su5ed.gregtechmod.api.cover.CoverType;
import dev.su5ed.gregtechmod.api.cover.ICoverable;
import dev.su5ed.gregtechmod.util.GtUtil;
import dev.su5ed.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

public class NormalCover extends BaseCover {
    public static final ResourceLocation TEXTURE_NORMAL = GtUtil.getCoverTexture("normal");
    public static final ResourceLocation TEXTURE_NOREDSTONE = GtUtil.getCoverTexture("noredstone");

    @NBTPersistent
    protected MeterCover.MeterMode mode = MeterCover.MeterMode.NORMAL;

    public NormalCover(ResourceLocation name, ICoverable be, Direction side, Item item) {
        super(name, be, side, item);
    }

    @Override
    public ResourceLocation getIcon() {
        return this.mode == MeterCover.MeterMode.NORMAL ? TEXTURE_NORMAL : TEXTURE_NOREDSTONE;
    }

    @Override
    public CoverType getType() {
        return CoverType.GENERIC;
    }

    @Override
    public boolean onScrewdriverClick(Player player) {
        this.mode = this.mode.next();
        this.be.updateRender();
        return true;
    }

    @Override
    public boolean letsRedstoneIn() {
        return this.mode == MeterCover.MeterMode.NORMAL;
    }

    @Override
    public boolean letsRedstoneOut() {
        return this.mode == MeterCover.MeterMode.NORMAL;
    }

    @Override
    public boolean allowEnergyTransfer() {
        return this.mode == MeterCover.MeterMode.INVERTED;
    }

    @Override
    public boolean letsLiquidsIn() {
        return this.mode == MeterCover.MeterMode.INVERTED;
    }

    @Override
    public boolean letsLiquidsOut() {
        return this.mode == MeterCover.MeterMode.INVERTED;
    }

    @Override
    public boolean letsItemsIn() {
        return this.mode == MeterCover.MeterMode.INVERTED;
    }

    @Override
    public boolean letsItemsOut() {
        return this.mode == MeterCover.MeterMode.INVERTED;
    }
}
