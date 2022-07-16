package dev.su5ed.gregtechmod.cover;

import dev.su5ed.gregtechmod.api.cover.CoverCategory;
import dev.su5ed.gregtechmod.api.cover.CoverType;
import dev.su5ed.gregtechmod.api.cover.ICoverable;
import dev.su5ed.gregtechmod.api.util.CoverInteractionResult;
import dev.su5ed.gregtechmod.util.GtLocale;
import dev.su5ed.gregtechmod.util.GtUtil;
import dev.su5ed.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

public class RedstoneSignalizerCover extends BaseCover {
    public static final ResourceLocation TEXTURE = GtUtil.getCoverTexture("redstone_signalizer");

    @NBTPersistent
    protected int signal;

    public RedstoneSignalizerCover(CoverType type, ICoverable te, Direction side, Item item) {
        super(type, te, side, item);
    }

    @Override
    protected CoverInteractionResult onClientScrewdriverClick(Player player) {
        return CoverInteractionResult.SUCCESS;
    }

    @Override
    protected CoverInteractionResult onServerScrewdriverClick(ServerPlayer player) {
        this.signal = (this.signal + 1) % 15;
        GtUtil.sendActionBarMessage(player, GtLocale.key("cover", "signal"), this.signal);
        return CoverInteractionResult.UPDATE;
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
    public CoverCategory getCategory() {
        return CoverCategory.METER;
    }
}
