package dev.su5ed.gregtechmod.api.util;

import net.minecraft.resources.ResourceLocation;

public final class Reference {
    public static final String NAME = "Gregtech Experimental";
    public static final String MODID = "gregtechmod";

    private Reference() {}
    
    public static ResourceLocation location(String... paths) {
        return new ResourceLocation(MODID, String.join("/", paths));
    }
}
