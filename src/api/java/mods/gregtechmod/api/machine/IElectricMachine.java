package mods.gregtechmod.api.machine;

public interface IElectricMachine extends IGregTechMachine {

    boolean addEnergy(double amount);

    double useEnergy(double amount, boolean simulate);
    
    default double useEnergy(double amount) {
        return useEnergy(amount, false);
    }
    
    boolean tryUseEnergy(double amount, boolean simulate);
    
    default boolean tryUseEnergy(double amount) {
        return tryUseEnergy(amount, false);
    }
    
    boolean canUseEnergy(double amount);

    int getSinkTier();

    int getSourceTier();

    double getMaxInputEUp();

    double getMaxOutputEUt();

    double getStoredEU();

    int getEUCapacity();

    double getAverageEUInput();

    double getAverageEUOutput();
    
    /**
     * <img src="https://i.imgur.com/7lZ0uZa.png" alt="Kaboom" width="384" height="382">
     */
    void markForExplosion();
    
    void markForExplosion(float power);
}
