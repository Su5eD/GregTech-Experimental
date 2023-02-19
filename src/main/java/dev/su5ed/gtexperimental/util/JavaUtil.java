package dev.su5ed.gtexperimental.util;

import dev.su5ed.gtexperimental.GregTechMod;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.Collection;

public final class JavaUtil {
    private static final DecimalFormat INT_FORMAT = new DecimalFormat("#,###,###,##0");

    public static void setStaticValue(Class<?> clazz, String fieldName, Object value) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(null, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            GregTechMod.LOGGER.catching(e);
        }
    }

    public static <T> boolean matchCollections(Collection<T> first, Collection<T> second) {
        return first.size() == second.size() || first.containsAll(second) && second.containsAll(first);
    }

    public static String formatNumber(int num) {
        return INT_FORMAT.format(num);
    }

    @Nullable
    public static <T extends Enum<T>> T getEnumConstantSafely(Class<T> clazz, String name) {
        try {
            return Enum.valueOf(clazz, name);
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    private JavaUtil() {}
}
