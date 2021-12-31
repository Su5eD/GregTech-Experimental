package mods.gregtechmod.util;

import net.minecraft.inventory.ClickType;

public enum ButtonClick {
    MOUSE_LEFT,
    MOUSE_RIGHT,
    SHIFT_MOVE;
    
    public static ButtonClick fromClickType(ClickType clickType, int dragType) {
        if (clickType == ClickType.QUICK_MOVE) return SHIFT_MOVE;
        
        return clickType == ClickType.PICKUP && dragType == 1 ? MOUSE_RIGHT : MOUSE_LEFT;
    }
}
