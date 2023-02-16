package dev.su5ed.gtexperimental.util;

import dev.su5ed.gtexperimental.GregTechMod;
import dev.su5ed.gtexperimental.api.Reference;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import one.util.streamex.StreamEx;

public final class GtLocale {

    private GtLocale() {}

    public static MutableComponent translateScan(String name, Object... args) {
        return key("scan", name).toComponent(args);
    }

    public static TranslationKey profileItemDescriptionKey(String name) {
        if (ProfileManager.INSTANCE.isClassic()) {
            TranslationKey classicKey = key("item", name, "classic_description");
            if (classicKey.exists()) {
                return classicKey; // FIXME this will crash servers wtf was I thinking
            }
        }
        return itemDescriptionKey(name);
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

    public static class TranslationKey {
        private final String key;

        public TranslationKey(String key) {
            this.key = key;
        }

        public boolean exists() {
            return I18n.exists(this.key);
        }

        public MutableComponent toComponent(Object... args) {
            return Component.translatable(this.key, args);
        }
    }
}
