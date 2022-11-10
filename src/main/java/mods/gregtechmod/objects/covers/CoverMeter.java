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

public abstract class CoverMeter extends CoverBase {
    @NBTPersistent
    protected MeterMode mode = MeterMode.NORMAL;

    public CoverMeter(ResourceLocation name, ICoverable te, EnumFacing side, ItemStack stack) {
        super(name, te, side, stack);
    }

    @Override
    public void doCoverThings() {
        if (te instanceof IGregTechMachine) {
            int strength = getRedstoneStrength();
            ((IGregTechMachine) te).setRedstoneOutput(side, mode == MeterMode.NORMAL ? strength : 15 - strength);
        }
    }

    @Override
    public boolean onScrewdriverClick(EntityPlayer player) {
        mode = mode.next();
        GtUtil.sendMessage(player, mode.getMessageKey());
        return true;
    }

    public abstract int getRedstoneStrength();

    public enum MeterMode {
        NORMAL,
        INVERTED;

        public static final MeterMode[] VALUES = values();

        public MeterMode next() {
            return VALUES[(ordinal() + 1) % VALUES.length];
        }

        public String getMessageKey() {
            return GtLocale.buildKey("cover", "mode", this.name().toLowerCase(Locale.ROOT));
        }
    }

    @Override
    public void onCoverRemove() {
        if (te instanceof IGregTechMachine) ((IGregTechMachine) te).setRedstoneOutput(side, 0);
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
    public CoverType getType() {
        return CoverType.METER;
    }
}
