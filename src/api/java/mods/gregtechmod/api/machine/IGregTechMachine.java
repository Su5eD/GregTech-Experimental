package mods.gregtechmod.api.machine;

import net.minecraft.util.EnumFacing;

public interface IGregTechMachine {
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
    
    void setAllowedToWork(boolean value);

    boolean isAllowedToWork();

    boolean isInputEnabled();
    
    void setInputEnabled(boolean value);

    boolean isOutputEnabled();
    
    void setOutputEnabled(boolean value);
    
    void markForExplosion();
}
