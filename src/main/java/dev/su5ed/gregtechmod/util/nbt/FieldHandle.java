package dev.su5ed.gregtechmod.util.nbt;

import java.lang.invoke.MethodHandle;
import java.util.function.Predicate;

record FieldHandle(String name, Class<?> type, Class<? extends NBTSerializer<?, ?>> serializer, Class<? extends NBTDeserializer<?, ?>> deserializer, Predicate<Object> optional, boolean modifyExisting, MethodHandle getter, MethodHandle setter) {

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
