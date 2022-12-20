package dev.su5ed.gregtechmod.api.machine;

public interface PowerProvider {
    int getPriority();
    
    double useEnergy(double amount, boolean simulate);
    
    double getStoredEnergy();
    
    double getCapacity();
}
