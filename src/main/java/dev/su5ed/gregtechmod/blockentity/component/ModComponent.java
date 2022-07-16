package dev.su5ed.gregtechmod.blockentity.component;

import dev.su5ed.gregtechmod.util.nbt.NBTSaveHandler;

public enum ModComponent {
    COVER_HANDLER(CoverHandler.class);
    
    private final Class<?> clazz;

    ModComponent(Class<?> clazz) {
        this.clazz = clazz;
    }
    
    public static void init() {
        for (ModComponent component : values()) {
            NBTSaveHandler.initClass(component.clazz);
        }
    }
}
