package dev.su5ed.gregtechmod.util.nbt;

import dev.su5ed.gregtechmod.util.Try;
import dev.su5ed.gregtechmod.util.nbt.NBTPersistent.Mode;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import one.util.streamex.StreamEx;

import javax.annotation.Nullable;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;
import java.util.*;

public final class NBTSaveHandler {
    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
    private static final Map<Class<?>, List<FieldHandle>> HANDLES = new HashMap<>();

    private NBTSaveHandler() {}

    public static void initClass(Class<?> clazz) {
        withParents(clazz)
            .remove(HANDLES::containsKey)
            .mapToEntry(cls -> StreamEx.of(cls.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(NBTPersistent.class))
                .map(Try.<Field, FieldHandle>of(field -> {
                        field.setAccessible(true);
                        NBTPersistent persistent = field.getAnnotation(NBTPersistent.class);
                        String annotatedName = persistent.name();
                        String name = !annotatedName.isEmpty() ? annotatedName : field.getName();
                        boolean modifyExisting = field.isAnnotationPresent(ModifyExisting.class);

                        Class<? extends NBTHandler<?, ?, ?>> handler = persistent.handler();
                        boolean customHandler = handler != Serializers.None.class;
                        Class<? extends NBTSerializer<?, ?>> serializer = customHandler ? handler : persistent.serializer();
                        Class<? extends NBTDeserializer<?, ?, ?>> deserializer = customHandler ? handler : persistent.deserializer();

                        VarHandle handle = MethodHandles
                            .privateLookupIn(cls, LOOKUP)
                            .unreflectVarHandle(field);

                        checkForDuplicateField(name, cls);
                        return new FieldHandle(persistent.mode(), name, field.getType(), handle, serializer, deserializer, persistent.include().predicate, modifyExisting);
                    })
                    .catching(field -> "Unable to create handle for field " + field.getName()))
                .toImmutableList()
            )
            .removeValues(List::isEmpty)
            .forKeyValue(HANDLES::put);
    }
    
    public static CompoundTag writeClassToNBT(Object instance) {
        return writeClassToNBT(instance, Mode.SAVE);
    }
    
    public static CompoundTag writeClassToNBT(Object instance, Mode mode) {
        return serializeFields(instance, mode, getFieldHandles(instance.getClass()));
    }

    public static void readClassFromNBT(Object instance, CompoundTag nbt) {
        getFieldHandles(instance.getClass())
            .cross(fieldHandle -> StreamEx.of(nbt.get(fieldHandle.getName()))
                .nonNull()
            )
            .forKeyValue((fieldHandle, nbtValue) -> {
                if (fieldHandle.modifyExisting) {
                    Object value = fieldHandle.getFieldValue(instance);
                    modifyExistingField(fieldHandle.type, value, nbtValue);
                }
                else {
                    Object deserialized = deserializeField(fieldHandle, nbtValue, instance);

                    if (deserialized != null) {
                        fieldHandle.setFieldValue(instance, deserialized);
                    }
                }
            });
    }

    public static CompoundTag serializeFields(Object instance, NBTPersistent.Mode mode, FieldHandle... fields) {
        return serializeFields(instance, mode, StreamEx.of(fields));
    }

    public static CompoundTag serializeFields(Object instance, NBTPersistent.Mode mode, StreamEx<FieldHandle> fields) {
        CompoundTag compound = new CompoundTag();
        fields
            .filter(field -> field.mode.accepts(mode))
            .mapToEntry(FieldHandle::getName, field -> serializeField(field, instance))
            .nonNullValues()
            .forKeyValue(compound::put);
        return compound;
    }

    @Nullable
    private static Tag serializeField(FieldHandle field, Object instance) {
        return field.getFieldValue(instance)
            .map(value -> serializeFieldValue(field, value))
            .orElse(null);
    }

    @Nullable
    private static Tag serializeFieldValue(FieldHandle field, Object value) {
        NBTSerializer<Object, Tag> serializer = field.serializer != Serializers.None.class
            ? NBTHandlerRegistry.getSpecialSerializer(field.serializer)
            : NBTHandlerRegistry.getSerializer(value);
        return serializer.serialize(value);
    }

    @Nullable
    private static Object deserializeField(FieldHandle field, Tag nbt, Object instance) {
        NBTDeserializer<Object, Tag, Object> deserializer = field.deserializer != Serializers.None.class
            ? NBTHandlerRegistry.getSpecialDeserializer(field.deserializer)
            : NBTHandlerRegistry.getDeserializer(field.type);
        return deserializer.deserialize(nbt, instance, field.type);
    }

    private static void modifyExistingField(Class<?> type, Object value, Tag nbt) {
        NBTHandlerRegistry.getModifyingDeserializer(type).modifyValue(value, nbt);
    }

    private static void checkForDuplicateField(String name, Class<?> cls) {
        boolean exists = getFieldHandles(cls)
            .map(FieldHandle::getName)
            .anyMatch(name::equals);

        if (exists) throw new RuntimeException("Duplicate field " + formatFieldName(cls, name));
    }

    public static FieldHandle getFieldHandle(String name, Object instance) {
        Class<?> cls = instance.getClass();
        return getFieldHandles(cls)
            .findFirst(field -> field.name.equals(name))
            .orElseThrow(() -> new IllegalArgumentException("Field handle for field " + formatFieldName(cls, name) + " not found"));
    }

    private static StreamEx<FieldHandle> getFieldHandles(Class<?> clazz) {
        return withParents(clazz)
            .map(HANDLES::get)
            .nonNull()
            .flatMap(Collection::stream);
    }

    public static StreamEx<Class<?>> withParents(Class<?> clazz) {
        return StreamEx.iterate(clazz, Objects::nonNull, Class::getSuperclass);
    }

    public static String formatFieldName(Class<?> clazz, String field) {
        return clazz.getName() + "#" + field;
    }
}
