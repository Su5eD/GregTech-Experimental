package dev.su5ed.gregtechmod.util;

import dev.su5ed.gregtechmod.api.util.Reference;
import net.minecraft.client.resources.language.I18n;
import one.util.streamex.StreamEx;

public final class GtLocale {
    
    private GtLocale() {}
    
    public static String translateItemDescription(String name) {
        return I18n.get(buildKey("item", name, "description"));
    }
    
    public static String buildKey(String prefix, String first, String... paths) {
        return StreamEx.of(prefix, Reference.MODID, first)
            .append(paths)
            .joining(".");
    }
}
