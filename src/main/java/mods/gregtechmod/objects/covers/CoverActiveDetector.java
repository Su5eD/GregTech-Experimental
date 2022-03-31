package mods.gregtechmod.objects.covers;

import mods.gregtechmod.api.cover.CoverType;
import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.machine.IGregTechMachine;
import mods.gregtechmod.api.machine.IMachineProgress;
import mods.gregtechmod.util.GtLocale;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import java.util.Locale;

public class CoverActiveDetector extends CoverGeneric {
    public static final ResourceLocation TEXTURE = GtUtil.getCoverTexture("active_detector");

    @NBTPersistent
    protected DetectorMode mode = DetectorMode.NORMAL;

    public CoverActiveDetector(ResourceLocation name, ICoverable te, EnumFacing side, ItemStack stack) {
        super(name, te, side, stack);
    }

    @Override
    public ResourceLocation getIcon() {
        return TEXTURE;
    }

    @Override
    public void doCoverThings() {
        if (te instanceof IMachineProgress) {
            IMachineProgress machine = (IMachineProgress) te;

            int strength = (int) ((machine.getProgress() + 4) / machine.getMaxProgress() * 15);
            if (mode == DetectorMode.NORMAL || mode == DetectorMode.INVERTED) {
                if (strength > 0 && machine.isActive()) {
                    machine.setRedstoneOutput(side, mode.inverted ? 15 - strength : strength);
                }
                else {
                    machine.setRedstoneOutput(side, mode.inverted ? 15 : 0);
                }
            }
            else {
                machine.setRedstoneOutput(side, (mode == DetectorMode.READY) != (machine.getProgress() == 0) ? 0 : 15);
            }
        }
    }

    @Override
    public boolean onScrewdriverClick(EntityPlayer player) {
        mode = mode.next();
        GtUtil.sendMessage(player, mode.getMessageKey());
        return true;
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
        if (te instanceof IGregTechMachine) ((IGregTechMachine) te).setRedstoneOutput(side, 0);
    }

    @Override
    public CoverType getType() {
        return CoverType.METER;
    }

    private enum DetectorMode {
        NORMAL,
        INVERTED(true),
        READY,
        NOT_READY;

        private static final DetectorMode[] VALUES = values();
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

        public String getMessageKey() {
            return GtLocale.buildKey("cover", "active_detector", "mode", name().toLowerCase(Locale.ROOT));
        }
    }
}
