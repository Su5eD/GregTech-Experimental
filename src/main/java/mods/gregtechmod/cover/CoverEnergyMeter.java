package mods.gregtechmod.cover;

import mods.gregtechmod.api.cover.CoverType;
import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.machine.IElectricalMachine;
import mods.gregtechmod.api.machine.IGregTechMachine;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import java.util.Locale;

public class CoverEnergyMeter extends CoverGeneric {
    protected Mode mode = Mode.UNIVERSAL;

    public CoverEnergyMeter(ICoverable te, EnumFacing side, ItemStack stack) {
        super(te, side, stack);
    }

    @Override
    public void doCoverThings() {
        if (!(te instanceof IElectricalMachine)) return;
        IElectricalMachine machine = (IElectricalMachine) te;
        byte strength;
        if (mode == Mode.AVERAGE_EU_IN || mode == Mode.AVERAGE_EU_IN_INVERTED) {
            strength = (byte) (machine.getAverageEUInput() / (machine.getMaxInputEUp() / 15));
        } else if (mode == Mode.AVERAGE_EU_OUT || mode == Mode.AVERAGE_EU_OUT_INVERTED) {
            strength = (byte) (machine.getAverageEUOutput() / (machine.getMaxOutputEUt() / 15));
        } else {
            double stored = -1;
            double capacity = 1;

            if (mode == Mode.UNIVERSAL || mode == Mode.UNIVERSAL_INVERTED) {
                stored = machine.getUniversalEnergy();
                capacity = machine.getUniversalEnergyCapacity();
            } else if(mode == Mode.ELECTRICITY || mode == Mode.ELECTRICITY_INVERTED) {
                stored = machine.getStoredEU();
                capacity = machine.getEUCapacity();
            } else if (mode == Mode.MJ || mode == Mode.MJ_INVERTED) {
                double mjCap = machine.getMjCapacity();
                if (mjCap > 0) {
                    stored = machine.getStoredMj();
                    capacity = machine.getMjCapacity();
                }
            } else if(mode == Mode.STEAM || mode == Mode.STEAM_INVERTED) {
                double steamCap = machine.getSteamCapacity();
                if (steamCap > 0) {
                    stored = machine.getStoredSteam();
                    capacity = machine.getSteamCapacity();
                }
            }
            strength = (byte) ((stored + 1) / capacity * 15);
        }

        if (strength > 0) {
            machine.setRedstoneOutput(side, mode.inverted ? (byte) (15 - strength) : strength);
        } else {
            machine.setRedstoneOutput(side, (byte) (mode.inverted ? 15 : 0));
        }
    }

    @Override
    public boolean onScrewdriverClick(EntityPlayer player) {
        mode = mode.next();
        GtUtil.sendMessage(player, mode.getMessageKey());
        return true;
    }

    @Override
    public boolean acceptsRedstone() {
        return true;
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
        if (te instanceof IGregTechMachine) {
            ((IGregTechMachine) te).setRedstoneOutput(side, (byte) 0);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger("mode", this.mode.ordinal());
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        this.mode = Mode.VALUES[nbt.getInteger("mode")];
    }

    @Override
    public ResourceLocation getIcon() {
        return new ResourceLocation(Reference.MODID, "blocks/covers/eu_meter");
    }

    @Override
    public int getTickRate() {
        return 5;
    }

    @Override
    public CoverType getType() {
        return CoverType.METER;
    }

    private enum Mode {
        UNIVERSAL,
        UNIVERSAL_INVERTED(true),
        ELECTRICITY,
        ELECTRICITY_INVERTED(true),
        MJ,
        MJ_INVERTED(true),
        STEAM,
        STEAM_INVERTED(true),
        AVERAGE_EU_IN,
        AVERAGE_EU_IN_INVERTED(true),
        AVERAGE_EU_OUT,
        AVERAGE_EU_OUT_INVERTED(true);

        public final boolean inverted;
        private static final Mode[] VALUES = values();

        Mode() {
            this(false);
        }

        Mode(boolean inverted) {
            this.inverted = inverted;
        }

        public Mode next() {
            return VALUES[(this.ordinal() + 1) % VALUES.length];
        }

        public String getMessageKey() {
            return Reference.MODID+".item.energy_meter.mode."+this.name().toLowerCase(Locale.ROOT);
        }
    }
}
