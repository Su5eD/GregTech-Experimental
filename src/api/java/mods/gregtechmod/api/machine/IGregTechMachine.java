package mods.gregtechmod.api.machine;

import net.minecraft.util.EnumFacing;

public interface IGregTechMachine {
    
    void setRedstoneOutput(EnumFacing side, int strength);
    
    void setAllowedToWork(boolean value);
    
    boolean workJustHasBeenEnabled();

    boolean isAllowedToWork();

    boolean isInputEnabled();
    
    void setInputEnabled(boolean value);

    boolean isOutputEnabled();
    
    void setOutputEnabled(boolean value);
}
