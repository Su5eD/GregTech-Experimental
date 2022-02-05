package dev.su5ed.gregtechmod.util;

import dev.su5ed.gregtechmod.api.util.Reference;
import one.util.streamex.StreamEx;

public final class Locale {
    
    private Locale() {}
    
    public static String buildKey(String... paths) {
        return StreamEx.of(Reference.MODID)
            .append(paths)
            .joining(".");
    }
}
