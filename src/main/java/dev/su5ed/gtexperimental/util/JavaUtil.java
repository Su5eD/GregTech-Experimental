package dev.su5ed.gtexperimental.util;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.Collection;

public final class JavaUtil {
    private static final DecimalFormat INT_FORMAT = new DecimalFormat("#,###,###,##0");
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,###,###,##0.00");

    public static <T> boolean matchCollections(Collection<T> first, Collection<T> second) {
        return first.size() == second.size() && first.containsAll(second) && second.containsAll(first);
    }

    public static String formatNumber(int num) {
        return INT_FORMAT.format(num);
    }

    public static String formatNumber(double num) {
        return num % 1 == 0 ? INT_FORMAT.format(num) : DECIMAL_FORMAT.format(num);
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
