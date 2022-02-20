package dev.su5ed.gregtechmod.cover;

import dev.su5ed.gregtechmod.api.cover.CoverType;
import dev.su5ed.gregtechmod.api.cover.ICoverable;
import dev.su5ed.gregtechmod.api.machine.IGregTechMachine;
import dev.su5ed.gregtechmod.api.machine.IMachineProgress;
import dev.su5ed.gregtechmod.util.GtLocale;
import dev.su5ed.gregtechmod.util.GtUtil;
import dev.su5ed.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Locale;

public class CoverActiveDetector extends CoverGeneric {
    public static final ResourceLocation TEXTURE = GtUtil.getCoverTexture("active_detector");

    @NBTPersistent
    protected DetectorMode mode = DetectorMode.NORMAL;

    public CoverActiveDetector(ResourceLocation name, ICoverable be, Direction side, ItemStack stack) {
        super(name, be, side, stack);
    }

    @Override
    public ResourceLocation getIcon() {
        return TEXTURE;
    }

    @Override
    public void doCoverThings() {
        if (this.be instanceof IMachineProgress machine) {
            int strength = (int) ((machine.getProgress() + 4) / machine.getMaxProgress() * 15);
            if (this.mode == DetectorMode.NORMAL || this.mode == DetectorMode.INVERTED) {
                int output = strength > 0 && machine.isActive()
                    ? this.mode.inverted ? 15 - strength : strength
                    : this.mode.inverted ? 15 : 0;
                machine.setRedstoneOutput(this.side, output);
            }
            else {
                machine.setRedstoneOutput(this.side, (this.mode == DetectorMode.READY) != (machine.getProgress() == 0) ? 0 : 15);
            }
        }
    }

    @Override
    public boolean onScrewdriverClick(Player player) {
        this.mode = this.mode.next();
        GtUtil.sendActionBarMessage(player, this.mode.getMessageKey());
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
        if (be instanceof IGregTechMachine machine) machine.setRedstoneOutput(side, 0);
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
