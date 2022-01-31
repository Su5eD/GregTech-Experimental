package mods.gregtechmod.util.nbt;

import java.lang.invoke.MethodHandle;
import java.util.function.Predicate;

class FieldHandle {
    final String name;
    final Class<?> type;
    final Class<? extends INBTSerializer<?, ?>> serializer;
    final Class<? extends INBTDeserializer<?, ?>> deserializer;
    final Predicate<Object> optional;
    final boolean modifyExisting;
    final MethodHandle getter;
    final MethodHandle setter;

    public FieldHandle(String name, Class<?> type, Class<? extends INBTSerializer<?, ?>> serializer, Class<? extends INBTDeserializer<?, ?>> deserializer, Predicate<Object> optional, boolean modifyExisting, MethodHandle getter, MethodHandle setter) {
        this.name = name;
        this.type = type;
        this.serializer = serializer;
        this.deserializer = deserializer;
        this.optional = optional;
        this.modifyExisting = modifyExisting;
        this.getter = getter;
        this.setter = setter;
    }

    public String getName() {
        return this.name;
    }

    public void setFieldValue(Object instance, Object value) {
        try {
            this.setter.invoke(instance, value);
        } catch (Throwable e) {
            throw new RuntimeException("Could not set field value", e);
        }
    }

    public Object getFieldValue(Object instance) {
        try {
            return this.getter.invoke(instance);
        } catch (Throwable e) {
            throw new RuntimeException("Could not get field value", e);
        }
    }
}
