package dev.su5ed.gtexperimental.api.machine;

public interface MachineProgress {
    boolean isActive();
    
    double getProgress();
    
    int getMaxProgress();
    
    void increaseProgress(double amount);
}
