package dev.su5ed.gregtechmod.util.nbt;

import java.lang.invoke.VarHandle;
import java.util.function.Predicate;

record FieldHandle(String name, Class<?> type, Class<? extends NBTSerializer<?, ?>> serializer, Class<? extends NBTDeserializer<?, ?>> deserializer, Predicate<Object> optional, boolean modifyExisting, VarHandle handle) {

    public void setFieldValue(Object instance, Object value) {
        try {
            this.handle.set(instance, value);
        } catch (Exception e) {
            throw new RuntimeException("Could not set field value", e);
        }
    }

    public Object getFieldValue(Object instance) {
        try {
            return this.handle.get(instance);
        } catch (Exception e) {
            throw new RuntimeException("Could not get field value", e);
        }
    }
}
