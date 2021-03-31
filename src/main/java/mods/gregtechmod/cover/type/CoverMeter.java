package mods.gregtechmod.cover.type;

import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.machine.IGregTechMachine;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Locale;

public abstract class CoverMeter extends CoverGeneric {
    protected MeterMode mode = MeterMode.NORMAL;

    public CoverMeter(ICoverable te, EnumFacing side, ItemStack stack) {
        super(te, side, stack);
    }

    @Override
    public void doCoverThings() {
        if (!(te instanceof IGregTechMachine)) return;

        Pair<Integer, Integer> info = getStorageAndCapacity();
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

    @Override
    public boolean onScrewdriverClick(EntityPlayer player) {
        mode = mode.next();
        GtUtil.sendMessage(player, mode.getMessageKey());
        return true;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setString("mode", this.mode.name());
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        this.mode = MeterMode.valueOf(nbt.getString("mode"));
    }

    protected abstract Pair<Integer, Integer> getStorageAndCapacity();

    public enum MeterMode {
        NORMAL,
        INVERTED;

        private static final MeterMode[] VALUES = values();

        public MeterMode next() {
            return VALUES[(this.ordinal() + 1) % VALUES.length];
        }

        public String getMessageKey() {
            return Reference.MODID+".cover.mode."+this.name().toLowerCase(Locale.ROOT);
        }
    }

    @Override
    public void onCoverRemoval() {
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
}
