package mods.gregtechmod.common.util;

import java.util.Random;
import java.util.function.BiPredicate;

public class GtUtil {
    public static Random RANDOM = new Random();

    public static <T, U> BiPredicate<T, U> alwaysTrue() {
        return (a, b) -> true;
    }

    public static String capitalizeString(String aString) {
        if (aString != null && aString.length() > 0) return aString.substring(0, 1).toUpperCase() + aString.substring(1);
        return "";
    }
}
