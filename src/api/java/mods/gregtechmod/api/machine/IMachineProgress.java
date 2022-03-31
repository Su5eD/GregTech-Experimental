package mods.gregtechmod.api.machine;

public interface IMachineProgress extends IGregTechMachine {
    boolean isActive();

    double getProgress();

    int getMaxProgress();

    void increaseProgress(double amount);
}
