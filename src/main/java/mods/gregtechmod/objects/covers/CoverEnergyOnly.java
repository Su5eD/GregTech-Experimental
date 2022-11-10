package mods.gregtechmod.objects.covers;

import mods.gregtechmod.api.cover.CoverType;
import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.machine.IGregTechMachine;
import mods.gregtechmod.util.GtLocale;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import java.util.Locale;

public class CoverEnergyOnly extends CoverBase {
    public static final ResourceLocation TEXTURE = GtUtil.getCoverTexture("energy_only");

    @NBTPersistent
    protected EnergyMode mode = EnergyMode.ALLOW;

    public CoverEnergyOnly(ResourceLocation name, ICoverable te, EnumFacing side, ItemStack stack) {
        super(name, te, side, stack);
    }

    @Override
    public ResourceLocation getIcon() {
        return TEXTURE;
    }

    @Override
    public boolean onScrewdriverClick(EntityPlayer player) {
        mode = mode.next();
        GtUtil.sendMessage(player, mode.getMessageKey());
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

        public String getMessageKey() {
            return GtLocale.buildKey("cover", "energy_mode", name().toLowerCase(Locale.ROOT));
        }
    }

    @Override
    public boolean allowEnergyTransfer() {
        return !(mode.conditional && te instanceof IGregTechMachine && ((IGregTechMachine) te).isAllowedToWork() == mode.inverted);
    }

    @Override
    public CoverType getType() {
        return CoverType.OTHER;
    }
}
