package dev.su5ed.gregtechmod.cover;

import dev.su5ed.gregtechmod.api.cover.CoverCategory;
import dev.su5ed.gregtechmod.api.cover.CoverType;
import dev.su5ed.gregtechmod.api.cover.Coverable;
import dev.su5ed.gregtechmod.api.machine.IGregTechMachine;
import dev.su5ed.gregtechmod.api.util.CoverInteractionResult;
import dev.su5ed.gregtechmod.util.GtLocale;
import dev.su5ed.gregtechmod.util.GtUtil;
import dev.su5ed.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

import java.util.Locale;

public class EnergyOnlyCover extends BaseCover {
    public static final ResourceLocation TEXTURE = GtUtil.getCoverTexture("energy_only");

    @NBTPersistent
    protected EnergyMode mode = EnergyMode.ALLOW;

    public EnergyOnlyCover(CoverType type, Coverable be, Direction side, Item item) {
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
        mode = mode.next();
        GtUtil.sendActionBarMessage(player, this.mode.getMessageKey());
        return CoverInteractionResult.UPDATE;
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

    @Override
    public boolean allowEnergyTransfer() {
        return !(this.mode.conditional && this.be instanceof IGregTechMachine machine && machine.isAllowedToWork() == this.mode.inverted);
    }

    @Override
    public CoverCategory getCategory() {
        return CoverCategory.OTHER;
    }
}
