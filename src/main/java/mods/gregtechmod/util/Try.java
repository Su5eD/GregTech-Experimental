package mods.gregtechmod.util;

import javax.annotation.Nullable;
import java.util.function.Function;

public class Try<T, R> implements Function<T, R> {
    private final CheckedFunction<T, R> tryFunc;
    @Nullable
    private Function<T, String> catchFunc;

    private Try(CheckedFunction<T, R> tryFunc) {
        this.tryFunc = tryFunc;
    }
    
    public static <T, R> Try<T, R> of(CheckedFunction<T, R> tryFunc) {
        return new Try<>(tryFunc);
    }
    
    public Try<T, R> catching(Function<T, String> catchFunc) {
        this.catchFunc = catchFunc;
        return this;
    }

    @Override
    public R apply(T t) {
        try {
            return this.tryFunc.apply(t);
        } catch (Exception e) {
            String message = this.catchFunc != null ? this.catchFunc.apply(t) : null;
            throw new RuntimeException(message, e);
        }
    }
}
