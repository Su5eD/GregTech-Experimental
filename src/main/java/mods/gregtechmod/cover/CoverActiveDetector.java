package mods.gregtechmod.cover;

import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.machine.IGregTechMachine;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import java.util.Locale;

public class CoverActiveDetector extends CoverGeneric {
    protected DetectorMode mode = DetectorMode.NORMAL;

    public CoverActiveDetector(ICoverable te, EnumFacing side, ItemStack stack) {
        super(te, side, stack);
    }

    @Override
    public ResourceLocation getIcon() {
        return new ResourceLocation(Reference.MODID, "blocks/covers/active_detector");
    }

    @Override
    public void doCoverThings() {
        if (!(te instanceof IGregTechMachine)) return;
        IGregTechMachine machine = (IGregTechMachine) te;

        byte strength = (byte) ((machine.getProgress() + 4) / machine.getMaxProgress() * 15);
        if (mode == DetectorMode.NORMAL || mode == DetectorMode.INVERTED) {
            if (strength > 0 && machine.isActive()) {
                machine.setRedstoneOutput(side, mode.inverted ? (byte) (15 - strength) : strength);
            } else {
                machine.setRedstoneOutput(side, (byte) (mode.inverted ? 15 : 0));
            }
        } else {
            machine.setRedstoneOutput(side, (mode == DetectorMode.READY) != (machine.getProgress() == 0) ? (byte) 0 : 15);
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
        this.mode = DetectorMode.valueOf(nbt.getString("mode"));
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
    public void onCoverRemoval() {
        if (te instanceof IGregTechMachine) ((IGregTechMachine) te).setRedstoneOutput(side, (byte) 0);
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
            return VALUES[(this.ordinal() + 1) % VALUES.length];
        }

        public String getMessageKey() {
            return Reference.MODID+".item.active_detector.mode."+this.name().toLowerCase(Locale.ROOT);
        }
    }
}
