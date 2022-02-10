package dev.su5ed.gregtechmod.util;

import java.util.function.Supplier;

public final class JavaUtil {

    private JavaUtil() {}

    public static <T> Supplier<T> nullSupplier() {
        return () -> null;
    }
}
