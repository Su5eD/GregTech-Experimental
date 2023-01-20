package dev.su5ed.gtexperimental.api.machine;

public interface IMachineProgress extends MachineController {
    boolean isActive();
    
    double getProgress();
    
    int getMaxProgress();
    
    void increaseProgress(double amount);
}
