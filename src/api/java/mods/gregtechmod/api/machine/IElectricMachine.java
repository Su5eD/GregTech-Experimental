package mods.gregtechmod.api.machine;

public interface IElectricMachine extends IGregTechMachine {

    void addEnergy(double amount);

    double useEnergy(double amount, boolean simulate);

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
