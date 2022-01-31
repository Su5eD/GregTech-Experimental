package mods.gregtechmod.util;

import net.minecraft.inventory.ClickType;

import javax.annotation.Nullable;

public enum ButtonClick {
    MOUSE_LEFT,
    MOUSE_RIGHT,
    SHIFT_MOVE;
    
    @Nullable
    public static ButtonClick fromClickType(ClickType clickType, int dragType) {
        if (clickType == ClickType.QUICK_MOVE) return SHIFT_MOVE;
        else if (clickType == ClickType.PICKUP) return dragType == 1 ? MOUSE_RIGHT : MOUSE_LEFT;
        
        return null;
    }
}
