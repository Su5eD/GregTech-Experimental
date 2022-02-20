package dev.su5ed.gregtechmod.util.nbt;

import dev.su5ed.gregtechmod.util.Try;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import one.util.streamex.StreamEx;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.*;

public final class NBTSaveHandler {
    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup(); // TODO Use VarHandle
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

                        Class<? extends NBTSerializer<?, ?>> serializer;
                        Class<? extends NBTDeserializer<?, ?>> deserializer;
                        Class<? extends NBTHandler<?, ?>> handler = persistent.handler();
                        if (handler != Serializers.None.class) {
                            serializer = handler;
                            deserializer = handler;
                        }
                        else {
                            serializer = persistent.serializer();
                            deserializer = persistent.deserializer();
                        }

                        checkForDuplicateField(name, cls);
                        return new FieldHandle(name, field.getType(), serializer, deserializer, persistent.include().predicate, modifyExisting, LOOKUP.unreflectGetter(field), LOOKUP.unreflectSetter(field));
                    })
                    .catching(field -> "Unable to create handle for field " + field.getName()))
                .toImmutableList()
            )
            .removeValues(List::isEmpty)
            .forKeyValue(HANDLES::put);
    }
    
    public static CompoundTag writeClassToNBT(Object instance) {
        CompoundTag tag = new CompoundTag();
        writeClassToNBT(instance, tag);
        return tag;
    }

    public static void writeClassToNBT(Object instance, CompoundTag nbt) {
        withFieldHandles(instance.getClass())
            .cross(fieldHandle -> StreamEx.of(fieldHandle.getFieldValue(instance))
                .filter(fieldHandle.optional())
            )
            .mapToValuePartial((fieldHandle, value) -> {
                Tag serialized;
                if (fieldHandle.serializer() != Serializers.None.class) {
                    serialized = NBTHandlerRegistry.getSpecialSerializer(fieldHandle.serializer()).serialize(value);
                }
                else serialized = serializeField(value);

                return Optional.ofNullable(serialized);
            })
            .mapKeys(FieldHandle::name)
            .forKeyValue(nbt::put);
    }

    public static void readClassFromNBT(Object instance, CompoundTag nbt) {
        withFieldHandles(instance.getClass())
            .cross(fieldHandle -> StreamEx.of(nbt.get(fieldHandle.name()))
                .nonNull()
            )
            .forKeyValue((fieldHandle, nbtValue) -> {
                if (fieldHandle.modifyExisting()) {
                    Object value = fieldHandle.getFieldValue(instance);
                    modifyExistingField(fieldHandle.type(), value, nbtValue);
                }
                else {
                    Object deserialized;
                    if (fieldHandle.deserializer() != Serializers.None.class) {
                        deserialized = NBTHandlerRegistry.getSpecialDeserializer(fieldHandle.deserializer()).deserialize(nbtValue, instance, fieldHandle.type());
                    }
                    else {
                        deserialized = deserializeField(nbtValue, instance, fieldHandle.type());
                    }

                    if (deserialized != null) fieldHandle.setFieldValue(instance, deserialized);
                }
            });
    }

    private static Tag serializeField(Object value) {
        return NBTHandlerRegistry.getSerializer(value).serialize(value);
    }

    private static Object deserializeField(Tag nbt, Object instance, Class<?> type) {
        return NBTHandlerRegistry.getDeserializer(type).deserialize(nbt, instance, type);
    }

    private static void modifyExistingField(Class<?> type, Object value, Tag nbt) {
        NBTHandlerRegistry.getModifyingDeserializer(type).modifyValue(value, nbt);
    }

    private static void checkForDuplicateField(String name, Class<?> cls) {
        boolean exists = withFieldHandles(cls)
            .map(FieldHandle::name)
            .anyMatch(name::equals);

        if (exists) throw new RuntimeException("Duplicate field " + formatFieldName(cls, name));
    }

    private static StreamEx<FieldHandle> withFieldHandles(Class<?> clazz) {
        return withParents(clazz)
            .map(HANDLES::get)
            .nonNull()
            .flatMap(Collection::stream);
    }

    private static StreamEx<Class<?>> withParents(Class<?> clazz) {
        return StreamEx.iterate(clazz, Objects::nonNull, Class::getSuperclass);
    }

    private static String formatFieldName(Class<?> clazz, String field) {
        return clazz.getName() + "#" + field;
    }
}
