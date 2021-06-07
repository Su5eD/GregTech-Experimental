package mods.gregtechmod.api.machine;

import net.minecraft.util.EnumFacing;

public interface IGregTechMachine {
    boolean isActive();

    double getProgress();

    int getMaxProgress();

    void increaseProgress(double amount);

    void setRedstoneOutput(EnumFacing side, byte strength);

    void addEnergy(double amount);

    double useEnergy(double amount, boolean simulate);

    /**
     * @return the most among stored EU, MJ, or steam
     */
    double getUniversalEnergy();

    /**
     * @return The maximum amount of energy this machine can store, either EU, MJ, or Steam converted to EU
     */
    double getUniversalEnergyCapacity();
    
    int getSinkTier();
    
    int getSourceTier();

    double getMaxInputEUp();

    double getMaxOutputEUp();

    double getStoredEU();

    double getEUCapacity();

    double getAverageEUInput();

    double getAverageEUOutput();

    double getStoredSteam();

    double getSteamCapacity();

    long getStoredMj();

    long getMjCapacity();

    void setMjCapacity(long capacity);

    void disableWorking();

    void enableWorking();

    boolean isAllowedToWork();

    boolean isInputEnabled();
    
    void disableInput();

    void enableInput();

    boolean isOutputEnabled();
    
    void disableOutput();

    void enableOutput();
    
    void markForExplosion();
}
