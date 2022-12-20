package dev.su5ed.gregtechmod.api.machine;

public interface IElectricMachine extends IGregTechMachine {
    boolean addEnergy(double amount);

    boolean canUseEnergy(double amount);

    default double useEnergy(double amount) {
        return useEnergy(amount, false);
    }

    double useEnergy(double amount, boolean simulate);

    default boolean tryUseEnergy(double amount) {
        return tryUseEnergy(amount, false);
    }
    
    boolean tryUseEnergy(double amount, boolean simulate);

    int getSinkTier();

    int getSourceTier();

    double getMaxInputEUp();

    double getMaxOutputEUt();

    double getStoredEnergy();

    double getEnergyCapacity();

    double getAverageEUInput();

    double getAverageEUOutput();

    void markForExplosion();
    
    void markForExplosion(float power);
}
