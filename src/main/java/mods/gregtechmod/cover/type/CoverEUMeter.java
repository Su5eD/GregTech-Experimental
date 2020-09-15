package mods.gregtechmod.cover.type;

import ic2.core.IC2;
import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.machine.IGregtechMachine;
import mods.gregtechmod.core.GregtechMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class CoverEUMeter extends CoverGeneric {
    protected byte mode;

    public CoverEUMeter(ICoverable te, EnumFacing side, ItemStack stack) {
        super(te, side, stack);
    }

    @Override
    public void doCoverThings() {
        if (!(te instanceof IGregtechMachine)) return;
        byte strength = 0;
        if (mode < 4) {
            double steamCap = ((IGregtechMachine) te).getSteamCapacity();

            if(mode < 2) strength = (byte) (((((IGregtechMachine) te).getStoredEU() + 1) / ((IGregtechMachine) te).getEUCapacity()) * 15);
            else if(steamCap > 0) strength = (byte) (((((IGregtechMachine) te).getStoredSteam() + 1) / ((IGregtechMachine) te).getSteamCapacity()) * 15);

            if (strength > 0) {
                ((IGregtechMachine) te).setRedstoneOutput(side, (byte) (mode%2 == 0 ? strength : (15 - strength)));
            } else {
                ((IGregtechMachine) te).setRedstoneOutput(side, (byte) (mode%2 == 0 ? 0 : 15));
            }
        } else if (mode < 6) {
            strength = (byte) (((IGregtechMachine)te).getAverageEUInput() / (((IGregtechMachine)te).getInputVoltage() / 15));
            if (strength > 0) {
                ((IGregtechMachine)te).setRedstoneOutput(side, mode%2==0 ? strength : (byte) (15 - strength));
            } else {
                ((IGregtechMachine)te).setRedstoneOutput(side, mode%2==0 ? (byte) 0 : 15);
            }
        }
    }

    @Override
    public boolean onScrewdriverClick(EntityPlayer player) {
        mode = (byte) ((mode + 1)%8);
        if (!player.world.isRemote) return true;
        switch (mode) {
            case 0:
                IC2.platform.messagePlayer(player, "Normal Electricity Storage");
                break;
            case 1:
                IC2.platform.messagePlayer(player, "Inverted Electricity Storage");
                break;
            case 2:
                IC2.platform.messagePlayer(player, "Normal Steam Storage");
                break;
            case 3:
                IC2.platform.messagePlayer(player, "Inverted Steam Storage");
                break;
            case 4:
                IC2.platform.messagePlayer(player, "Normal Average Electric Input");
                break;
            case 5:
                IC2.platform.messagePlayer(player, "Inverted Average Electric Input");
                break;
            case 6:
                IC2.platform.messagePlayer(player, "Normal Average Electric Output");
                break;
            case 7:
                IC2.platform.messagePlayer(player, "Inverted Average Electric Output");
                break;
        }
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
        if (te instanceof IGregtechMachine) {
            ((IGregtechMachine) te).setRedstoneOutput(side, (byte) 0);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setByte("mode", this.mode);
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        this.mode = nbt.getByte("mode");
    }

    @Override
    public ResourceLocation getIcon() {
        return new ResourceLocation(GregtechMod.MODID, "blocks/covers/eu_meter");
    }

    @Override
    public short getTickRate() {
        return 5;
    }
}
