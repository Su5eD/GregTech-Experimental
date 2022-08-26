package dev.su5ed.gregtechmod.network;

import java.lang.invoke.VarHandle;
import java.util.Optional;

public class FieldHandle {
    public final String name;
    public final Class<?> type;
    private final VarHandle handle;

    public FieldHandle(String name, Class<?> type, VarHandle handle) {
        this.name = name;
        this.type = type;
        this.handle = handle;
    }

    public void setValue(Object instance, Object value) {
        try {
            this.handle.set(instance, value);
        } catch (Exception e) {
            throw new RuntimeException("Could not set field value", e);
        }
    }

    public Optional<?> getValue(Object instance) {
        try {
            Object value = this.handle.get(instance);
            return Optional.ofNullable(value);
        } catch (Exception e) {
            throw new RuntimeException("Could not get field value", e);
        }
    }
}
