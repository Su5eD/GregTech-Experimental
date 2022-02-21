package dev.su5ed.gregtechmod.util.nbt;

import java.lang.invoke.VarHandle;
import java.util.Optional;
import java.util.function.Predicate;

public class FieldHandle {
    public final NBTPersistent.Mode mode;
    public final String name;
    public final Class<?> type;
    private final VarHandle handle;
    public final Class<? extends NBTSerializer<?, ?>> serializer;
    public final Class<? extends NBTDeserializer<?, ?, ?>> deserializer;
    public final Predicate<Object> optional;
    public final boolean modifyExisting;

    public FieldHandle(NBTPersistent.Mode mode, String name, Class<?> type, VarHandle handle, Class<? extends NBTSerializer<?, ?>> serializer, Class<? extends NBTDeserializer<?, ?, ?>> deserializer, Predicate<Object> optional, boolean modifyExisting) {
        this.mode = mode;
        this.name = name;
        this.type = type;
        this.handle = handle;
        this.serializer = serializer;
        this.deserializer = deserializer;
        this.optional = optional;
        this.modifyExisting = modifyExisting;
    }

    public String getName() {
        return this.name;
    }

    public void setFieldValue(Object instance, Object value) {
        try {
            this.handle.set(instance, value);
        } catch (Exception e) {
            throw new RuntimeException("Could not set field value", e);
        }
    }

    public Optional<?> getFieldValue(Object instance) {
        try {
            Object value = this.handle.get(instance);
            return Optional.of(value).filter(this.optional);
        } catch (Exception e) {
            throw new RuntimeException("Could not get field value", e);
        }
    }
}
