package mods.gregtechmod.cover;

import mods.gregtechmod.api.cover.CoverType;
import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.machine.IGregTechMachine;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Locale;

public abstract class CoverMeter extends CoverGeneric {
    @NBTPersistent
    protected MeterMode mode = MeterMode.NORMAL;

    public CoverMeter(ResourceLocation name, ICoverable te, EnumFacing side, ItemStack stack) {
        super(name, te, side, stack);
    }

    @Override
    public void doCoverThings() {
        if (te instanceof IGregTechMachine) {
            Pair<Integer, Integer> info = getItemStorageAndCapacity();
            int stored = info.getLeft();
            int capacity = info.getRight();
            
            if (capacity > 0) {
                capacity /= 15;
                double strength = stored / (double) capacity;
                ((IGregTechMachine)te).setRedstoneOutput(side, (byte) (mode == MeterMode.NORMAL ? strength : 15 - strength));
            } else {
                ((IGregTechMachine)te).setRedstoneOutput(side, (byte) (mode == MeterMode.INVERTED ? 0 : 15));
            }
        }
    }

    @Override
    public boolean onScrewdriverClick(EntityPlayer player) {
        mode = mode.next();
        GtUtil.sendMessage(player, mode.getMessageKey());
        return true;
    }

    protected abstract Pair<Integer, Integer> getItemStorageAndCapacity();

    public enum MeterMode {
        NORMAL,
        INVERTED;

        public static final MeterMode[] VALUES = values();

        public MeterMode next() {
            return VALUES[(this.ordinal() + 1) % VALUES.length];
        }

        public String getMessageKey() {
            return Reference.MODID + ".cover.mode." + this.name().toLowerCase(Locale.ROOT);
        }
    }

    @Override
    public void onCoverRemove() {
        if (te instanceof IGregTechMachine) ((IGregTechMachine) te).setRedstoneOutput(side, (byte) 0);
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
