package dev.su5ed.gregtechmod.util;

import dev.su5ed.gregtechmod.GregTechMod;

import java.lang.reflect.Field;

public final class JavaUtil {

    private JavaUtil() {}

    public static void setStaticValue(Class<?> clazz, String fieldName, Object value) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(null, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            GregTechMod.LOGGER.catching(e);
        }
    }
}