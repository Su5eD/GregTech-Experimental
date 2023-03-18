package dev.su5ed.gtexperimental.util;

import dev.su5ed.gtexperimental.api.Reference;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import one.util.streamex.StreamEx;

public final class GtLocale {

    private GtLocale() {}
    
    public static MutableComponent formatNumber(int number) {
        if (number >= 1000000) {
            return Component.literal(number / (double) 1000000 + "M");
        }
        else if (number >= 1000) {
            return Component.literal(number / (double) 1000 + "k");
        }
        return Component.literal(String.valueOf(number));
    }

    public static MutableComponent translateScan(String name, Object... args) {
        return key("scan", name).toComponent(args);
    }

    public static MutableComponent translateGenericDescription(String name, Object... params) {
        return key("generic", name, "description").toComponent(params);
    }

    public static MutableComponent translateItemDescription(String name, Object... params) {
        return itemDescriptionKey(name).toComponent(params);
    }

    public static TranslationKey itemDescriptionKey(String name) {
        return key("item", name, "description");
    }

    public static TranslationKey itemKey(String name, String... paths) {
        return key("item", name, paths);
    }

    public static TranslationKey key(String prefix, String first, String... paths) {
        String key = StreamEx.of(prefix, Reference.MODID, first)
            .append(paths)
            .joining(".");
        return new TranslationKey(key);
    }

    public record TranslationKey(String key) {
        public MutableComponent toComponent(Object... args) {
            return Component.translatable(this.key, args);
        }
    }
}
