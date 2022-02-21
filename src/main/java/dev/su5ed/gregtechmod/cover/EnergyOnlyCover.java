package dev.su5ed.gregtechmod.cover;

import dev.su5ed.gregtechmod.api.cover.CoverType;
import dev.su5ed.gregtechmod.api.cover.ICoverable;
import dev.su5ed.gregtechmod.api.machine.IGregTechMachine;
import dev.su5ed.gregtechmod.util.GtLocale;
import dev.su5ed.gregtechmod.util.GtUtil;
import dev.su5ed.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Locale;

public class EnergyOnlyCover extends GenericCover {
    public static final ResourceLocation TEXTURE = GtUtil.getCoverTexture("energy_only");

    @NBTPersistent
    protected EnergyMode mode = EnergyMode.ALLOW;

    public EnergyOnlyCover(ResourceLocation name, ICoverable be, Direction side, ItemStack stack) {
        super(name, be, side, stack);
    }

    @Override
    public ResourceLocation getIcon() {
        return TEXTURE;
    }

    @Override
    public boolean onScrewdriverClick(Player player) {
        mode = mode.next();
        GtUtil.sendActionBarMessage(player, this.mode.getMessageKey());
        return true;
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
    public CoverType getType() {
        return CoverType.OTHER;
    }
}
