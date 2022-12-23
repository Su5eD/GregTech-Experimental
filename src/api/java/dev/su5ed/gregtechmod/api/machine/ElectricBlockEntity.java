package dev.su5ed.gregtechmod.api.machine;

import net.minecraft.core.Direction;

import java.util.Collection;

public interface ElectricBlockEntity extends IGregTechMachine {
    Collection<Direction> getSinkSides();

    Collection<Direction> getSourceSides();

    int getSinkTier();

    int getSourceTier();
    
    int getSourcePackets();

    double getMaxOutputEUp();
    
    int getEnergyCapacity();
    
    double getMinimumStoredEnergy();
    
    void configurePowerProvider(PowerProvider provider);
}
