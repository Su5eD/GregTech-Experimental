package dev.su5ed.gtexperimental.cover;

import dev.su5ed.gtexperimental.api.cover.CoverInteractionResult;
import dev.su5ed.gtexperimental.api.cover.CoverType;
import dev.su5ed.gtexperimental.api.util.FriendlyCompoundTag;
import dev.su5ed.gtexperimental.util.GtLocale;
import dev.su5ed.gtexperimental.util.GtUtil;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Locale;

public abstract class MeterCover extends BaseCover<BlockEntity> {
    protected MeterMode mode = MeterMode.NORMAL;

    protected MeterCover(CoverType<BlockEntity> type, BlockEntity be, Direction side, Item item) {
        super(type, be, side, item);
    }

    @Override
    public void tick() {
        int strength = getRedstoneStrength();
        this.machineController.setRedstoneOutput(this.side, this.mode == MeterMode.NORMAL ? strength : 15 - strength);
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

    public abstract int getRedstoneStrength();

    @Override
    public void onCoverRemove() {
        this.machineController.setRedstoneOutput(this.side, 0);
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
    public void save(FriendlyCompoundTag tag) {
        super.save(tag);
        tag.putEnum("mode", this.mode);
    }

    @Override
    public void load(FriendlyCompoundTag tag) {
        super.load(tag);
        this.mode = tag.getEnum("mode");
    }

    public enum MeterMode {
        NORMAL,
        INVERTED;

        public static final MeterMode[] VALUES = values();

        public MeterMode next() {
            return VALUES[(ordinal() + 1) % VALUES.length];
        }

        public GtLocale.TranslationKey getMessageKey() {
            return GtLocale.key("cover", "mode", name().toLowerCase(Locale.ROOT));
        }
    }
}
