package dev.su5ed.gregtechmod.api.machine;

public interface PowerProvider {
    int getPriority();

    default double useEnergy(double amount) {
        return useEnergy(amount, false);
    }

    double useEnergy(double amount, boolean simulate);

    double getStoredEnergy();

    int getCapacity();
}
