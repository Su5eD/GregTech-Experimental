package dev.su5ed.gregtechmod.cover;

import dev.su5ed.gregtechmod.api.cover.CoverType;
import dev.su5ed.gregtechmod.api.machine.IMachineProgress;
import dev.su5ed.gregtechmod.api.cover.CoverInteractionResult;
import dev.su5ed.gregtechmod.api.util.FriendlyCompoundTag;
import dev.su5ed.gregtechmod.util.GtLocale;
import dev.su5ed.gregtechmod.util.GtUtil;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

import java.util.Locale;

public class ActiveDetectorCover extends BaseCover<IMachineProgress> {
    public static final ResourceLocation TEXTURE = GtUtil.getCoverTexture("active_detector");

    protected DetectorMode mode = DetectorMode.NORMAL;

    public ActiveDetectorCover(CoverType<IMachineProgress> type, IMachineProgress be, Direction side, Item item) {
        super(type, be, side, item);
    }

    @Override
    public ResourceLocation getIcon() {
        return TEXTURE;
    }

    @Override
    public void tick() {
        int strength = (int) ((this.be.getProgress() + 4) / this.be.getMaxProgress() * 15);
        if (this.mode == DetectorMode.NORMAL || this.mode == DetectorMode.INVERTED) {
            int output = strength > 0 && this.be.isActive()
                ? this.mode.inverted ? 15 - strength : strength
                : this.mode.inverted ? 15 : 0;
            this.be.setRedstoneOutput(this.side, output);
        }
        else {
            this.be.setRedstoneOutput(this.side, (this.mode == DetectorMode.READY) != (this.be.getProgress() == 0) ? 0 : 15);
        }
    }

    @Override
    protected CoverInteractionResult onClientScrewdriverClick(Player player) {
        return CoverInteractionResult.SUCCESS;
    }

    @Override
    protected CoverInteractionResult onServerScrewdriverClick(ServerPlayer player) {
        this.mode = this.mode.next();
        GtUtil.sendActionBarMessage(player, this.mode.getMessageKey());
        return CoverInteractionResult.CHANGED;
    }

    @Override
    public int getTickRate() {
        return 5;
    }

    @Override
    public boolean allowEnergyTransfer() {
        return true;
    }

    @Override
    public boolean letsLiquidsIn() {
        return true;
    }

    @Override
    public boolean letsLiquidsOut() {
        return true;
    }

    @Override
    public boolean letsItemsIn() {
        return true;
    }

    @Override
    public boolean letsItemsOut() {
        return true;
    }

    @Override
    public boolean overrideRedstoneOut() {
        return true;
    }

    @Override
    public void onCoverRemove() {
        this.be.setRedstoneOutput(side, 0);
    }

    @Override
    public void save(FriendlyCompoundTag tag) {
        super.save(tag);
        tag.putEnum("mode", this.mode);
    }

    @Override
    public void load(FriendlyCompoundTag tag) {
        super.load(tag);
        this.mode = tag.getEnum("mode");
    }

    private enum DetectorMode {
        NORMAL,
        INVERTED(true),
        READY,
        NOT_READY;

        public static final DetectorMode[] VALUES = values();

        public final boolean inverted;

        DetectorMode() {
            this(false);
        }

        DetectorMode(boolean inverted) {
            this.inverted = inverted;
        }

        public DetectorMode next() {
            return VALUES[(ordinal() + 1) % VALUES.length];
        }

        public GtLocale.TranslationKey getMessageKey() {
            return GtLocale.key("cover", "active_detector", "mode", name().toLowerCase(Locale.ROOT));
        }
    }
}
