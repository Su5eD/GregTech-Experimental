package dev.su5ed.gregtechmod.util;

import dev.su5ed.gregtechmod.GregTechMod;
import dev.su5ed.gregtechmod.api.util.Reference;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TranslatableComponent;
import one.util.streamex.StreamEx;

public final class GtLocale {

    private GtLocale() {}

    public static String translateProfileItemDescription(String name) {
        if (GregTechMod.isClassic) {
            TranslationKey classicKey = key("item", name, "classic_description");
            if (classicKey.exists()) return classicKey.translate();
        }
        return translateItemDescription(name);
    }

    public static String translateItemDescription(String name) {
        return itemDescriptionKey(name).translate();
    }

    public static String translateGenericDescription(String name, Object... params) {
        return key("generic", name, "description").translate(params);
    }
    
    public static TranslationKey itemDescriptionKey(String name) {
        return key("item", name, "description");
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

        public String translate(Object... params) {
            return I18n.get(this.key, params);
        }

        public TranslatableComponent toComponent(Object... args) {
            return new TranslatableComponent(this.key, args);
        }
    }
}
