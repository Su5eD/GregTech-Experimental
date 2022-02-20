package dev.su5ed.gregtechmod.cover;

import dev.su5ed.gregtechmod.api.cover.CoverType;
import dev.su5ed.gregtechmod.api.cover.ICoverable;
import dev.su5ed.gregtechmod.util.GtLocale;
import dev.su5ed.gregtechmod.util.GtUtil;
import dev.su5ed.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class CoverRedstoneSignalizer extends CoverGeneric {
    public static final ResourceLocation TEXTURE = GtUtil.getCoverTexture("redstone_signalizer");

    @NBTPersistent
    protected int signal;

    public CoverRedstoneSignalizer(ResourceLocation name, ICoverable te, Direction side, ItemStack stack) {
        super(name, te, side, stack);
    }

    @Override
    public boolean onScrewdriverClick(Player player) {
        this.signal = (this.signal + 1) % 15;
        GtUtil.sendActionBarMessage(player, GtLocale.key("cover", "signal"), this.signal);
        return true;
    }

    @Override
    public int getRedstoneInput() {
        return this.signal;
    }

    @Override
    public ResourceLocation getIcon() {
        return TEXTURE;
    }

    @Override
    public CoverType getType() {
        return CoverType.METER;
    }
}
