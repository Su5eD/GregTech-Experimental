package dev.su5ed.gregtechmod.api;

import net.minecraft.resources.ResourceLocation;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Reference {
    public static final String NAME = "Gregtech Experimental";
    public static final String MODID = "gregtechmod";

    private Reference() {}
    
    public static ResourceLocation location(String... paths) {
        return new ResourceLocation(MODID, String.join("/", paths));
    }
    
    public static ResourceLocation locationNullable(String... paths) {
        return new ResourceLocation(MODID, Stream.of(paths).filter(Objects::nonNull).collect(Collectors.joining("/")));
    }
}
