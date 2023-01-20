package dev.su5ed.gtexperimental.api.machine;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Collection;

public interface ElectricBlockEntity {
    default BlockEntity be() {
        return (BlockEntity) this;
    }
    
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
