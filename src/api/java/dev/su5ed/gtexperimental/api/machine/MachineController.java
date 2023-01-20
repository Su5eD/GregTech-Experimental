package dev.su5ed.gtexperimental.api.machine;

import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public interface MachineController extends ICapabilityProvider {
    
    int getSignal(Direction side);

    void setRedstoneOutput(Direction side, int strength);

    boolean isAllowedToWork();

    void setAllowedToWork(boolean value);

    boolean workJustHasBeenEnabled();

    boolean isInputEnabled();
    
    void setInputEnabled(boolean value);

    boolean isOutputEnabled();
    
    void setOutputEnabled(boolean value);
}
