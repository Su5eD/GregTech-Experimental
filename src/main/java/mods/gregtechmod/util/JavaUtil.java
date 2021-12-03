package mods.gregtechmod.util;

import com.google.common.base.Stopwatch;
import mods.gregtechmod.core.GregTechMod;

import javax.annotation.Nullable;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.BiPredicate;
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

    public static <T, U> BiPredicate<T, U> alwaysTrue() {
        return (a, b) -> true;
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
        List<T> list = new ArrayList<>();
        iterable.forEach(list::add);
        return Collections.unmodifiableList(list);
    }

    public static <T> List<T> mergeCollection(Collection<T> first, Collection<T> second) {
        return Stream.of(first, second)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
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

    public static Path copyDir(Path source, Path target) {
        try(DirectoryStream<Path> stream = Files.newDirectoryStream(source)) {
            for (Path path : stream) {
                Path dest = Paths.get(target.toString(), path.getFileName().toString());
                GregTechMod.LOGGER.debug("Copying file " + path + " to " + dest);
                if (!Files.exists(dest)) {
                    Files.copy(path, dest);
                    if (Files.isDirectory(path)) copyDir(path, dest);
                }
            }
            return target;
        } catch (IOException e) {
            GregTechMod.LOGGER.catching(e);
            return null;
        }
    }

    public static void measureTime(String name, Runnable runnable) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        runnable.run();
        stopwatch.stop();
        GregTechMod.LOGGER.debug(name + " took " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + " ms");
    }
}
