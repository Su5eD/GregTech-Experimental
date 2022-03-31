package mods.gregtechmod.util;

import java.util.function.Supplier;

public class LazyValue<T> {
    private final Supplier<T> factory;
    private T instance;

    public LazyValue(Supplier<T> factory) {
        this.factory = factory;
    }

    public T get() {
        if (this.instance == null) {
            this.instance = this.factory.get();
        }
        return this.instance;
    }
}
