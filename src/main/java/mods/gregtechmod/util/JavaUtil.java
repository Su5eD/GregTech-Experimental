package mods.gregtechmod.util;

import com.google.common.base.Stopwatch;
import mods.gregtechmod.core.GregTechMod;
import one.util.streamex.StreamEx;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class JavaUtil {
    public static final Supplier<String> NULL_SUPPLIER = () -> null;
    public static final Random RANDOM = new Random();

    private static final DecimalFormat INT_FORMAT = new DecimalFormat("#,###,###,##0");
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,###,###,##0.00");

    private JavaUtil() {}

    public static <T> Predicate<T> alwaysTrue() {
        return o -> true;
    }

    public static <T> Predicate<T> alwaysFalse() {
        return o -> false;
    }

    public static <T, U> BiPredicate<T, U> alwaysTrueBi() {
        return (a, b) -> true;
    }

    public static <T, U> BiPredicate<T, U> alwaysFalseBi() {
        return (a, b) -> false;
    }

    public static String capitalizeString(String str) {
        return str != null && str.length() > 0 ? str.substring(0, 1).toUpperCase() + str.substring(1) : "";
    }

    public static String formatNumber(int num) {
        return INT_FORMAT.format(num);
    }

    public static String formatNumber(double num) {
        if (num % 1 == 0) return INT_FORMAT.format(num);
        else return DECIMAL_FORMAT.format(num);
    }

    @SafeVarargs
    public static <T> List<T> nonNullList(T... elements) {
        return Stream.of(elements)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    public static <T> void fillEmptyList(List<T> list, T fill, int maxSize) {
        int space = maxSize - 1 - list.size();
        for (int i = 0; i < space; i++) {
            list.add(fill);
        }
    }

    public static <T> List<T> toList(Iterable<T> iterable) {
        return StreamEx.of(iterable.iterator())
            .toImmutableList();
    }

    public static <T> List<T> mergeCollection(Collection<T> first, Collection<T> second) {
        return StreamEx.of(first, second)
            .flatMap(Collection::stream)
            .toImmutableList();
    }

    public static <T> boolean matchCollections(Collection<T> first, Collection<T> second) {
        return first.size() == second.size() || first.containsAll(second) && second.containsAll(first);
    }

    public static <K, V> Map<K, V> zipToMap(List<K> keys, List<V> values) {
        if (keys.size() != values.size()) throw new IllegalArgumentException("Lists must be the same size");

        Iterator<K> keyIt = keys.iterator();
        Iterator<V> valIt = values.iterator();
        return IntStream.range(0, keys.size()).boxed()
            .collect(Collectors.toMap(_i -> keyIt.next(), _i -> valIt.next()));
    }

    @Nullable
    public static <T extends Enum<T>> T getEnumConstantSafely(Class<T> clazz, String name) {
        try {
            return Enum.valueOf(clazz, name);
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    public static void setStaticValue(Class<?> clazz, String fieldName, Object value) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(null, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            GregTechMod.LOGGER.catching(e);
        }
    }

    public static void measureTime(String name, Runnable runnable) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        runnable.run();
        stopwatch.stop();
        GregTechMod.LOGGER.debug(name + " took " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + " ms");
    }

    public static int log2(int a) {
        return (int) (Math.log(a) / Math.log(2));
    }
}
