package mods.gregtechmod.api.machine;

import net.minecraft.core.Direction;

public interface IGregTechMachine {
    
    void setRedstoneOutput(Direction side, int strength);
    
    void setAllowedToWork(boolean value);
    
    boolean workJustHasBeenEnabled();

    boolean isAllowedToWork();

    boolean isInputEnabled();
    
    void setInputEnabled(boolean value);

    boolean isOutputEnabled();
    
    void setOutputEnabled(boolean value);
}
