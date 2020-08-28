package mods.gregtechmod.api.machine;

import net.minecraft.util.EnumFacing;

public interface IGregtechMachine {
    //TODO: Documentation
    boolean isActive();

    double getProgress();

    int getMaxProgress();

    void increaseProgress(double amount);

    void setRedstoneOutput(EnumFacing side, byte strength);

    double addEnergy(double amount);

    double useEnergy(double amount, boolean simulate);

    /**
     * @return stored steam or energy
     */
    double getUniversalEnergy();

    double getUniversalEnergyCapacity();

    double getInputVoltage();

    double getStoredEU();

    double getEUCapacity();

    int getAverageEUInput();

    int getAverageEUOutput();

    double getStoredSteam();

    double getSteamCapacity();

    void markForCoverBehaviorUpdate();

    void disableWorking();

    void enableWorking();

    boolean isAllowedToWork();

    void disableInput();

    void enableInput();

    boolean isInputEnabled();

    void disableOutput();

    void enableOutput();

    boolean isOutputEnabled();
}
