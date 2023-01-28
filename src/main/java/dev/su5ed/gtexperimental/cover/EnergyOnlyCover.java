package dev.su5ed.gtexperimental.cover;

import dev.su5ed.gtexperimental.api.cover.CoverInteractionResult;
import dev.su5ed.gtexperimental.api.cover.CoverType;
import dev.su5ed.gtexperimental.api.util.FriendlyCompoundTag;
import dev.su5ed.gtexperimental.util.GtLocale;
import dev.su5ed.gtexperimental.util.GtUtil;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Locale;

public class EnergyOnlyCover extends BaseCover<BlockEntity> {
    public static final ResourceLocation TEXTURE = GtUtil.getCoverTexture("energy_only");

    protected EnergyMode mode = EnergyMode.ALLOW;

    public EnergyOnlyCover(CoverType<BlockEntity> type, BlockEntity be, Direction side, Item item) {
        super(type, be, side, item);
    }

    @Override
    public ResourceLocation getIcon() {
        return TEXTURE;
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
    public boolean allowEnergyTransfer() {
        return !(this.mode.conditional && this.machineController.isAllowedToWork() == this.mode.inverted);
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

    private enum EnergyMode {
        ALLOW,
        ALLOW_CONDITIONAL(true),
        DISALLOW_CONDITIONAL(true, true);

        private static final EnergyMode[] VALUES = values();
        public final boolean conditional;
        public final boolean inverted;

        EnergyMode() {
            this(false, false);
        }

        EnergyMode(boolean conditional) {
            this(conditional, false);
        }

        EnergyMode(boolean conditional, boolean inverted) {
            this.conditional = conditional;
            this.inverted = inverted;
        }

        public EnergyMode next() {
            return VALUES[(ordinal() + 1) % VALUES.length];
        }

        public GtLocale.TranslationKey getMessageKey() {
            return GtLocale.key("cover", "energy_mode", name().toLowerCase(Locale.ROOT));
        }
    }
}
