package dev.su5ed.gregtechmod.api.machine;

import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public interface IGregTechMachine extends ICapabilityProvider { // TODO extend IForgeBlockEntity?, remove I prefix
    
    void setRedstoneOutput(Direction side, int strength);
    
    void setAllowedToWork(boolean value);
    
    boolean workJustHasBeenEnabled();

    boolean isAllowedToWork();

    boolean isInputEnabled();
    
    void setInputEnabled(boolean value);

    boolean isOutputEnabled();
    
    void setOutputEnabled(boolean value);
}
