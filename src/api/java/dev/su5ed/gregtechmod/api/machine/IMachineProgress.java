package dev.su5ed.gregtechmod.api.machine;

public interface IMachineProgress extends MachineController {
    boolean isActive();
    
    double getProgress();
    
    int getMaxProgress();
    
    void increaseProgress(double amount);
}
