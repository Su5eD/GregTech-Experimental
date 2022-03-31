package mods.gregtechmod.util.nbt;

import mods.gregtechmod.util.Try;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import one.util.streamex.StreamEx;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.*;

public final class NBTSaveHandler {
    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
    private static final Map<Class<?>, List<FieldHandle>> HANDLES = new HashMap<>();

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

                        Class<? extends INBTSerializer<?, ?>> serializer;
                        Class<? extends INBTDeserializer<?, ?>> deserializer;
                        Class<? extends INBTHandler<?, ?>> handler = persistent.handler();
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

    public static void writeClassToNBT(Object instance, NBTTagCompound nbt) {
        withFieldHandles(instance.getClass())
            .cross(fieldHandle -> StreamEx.of(fieldHandle.getFieldValue(instance))
                .filter(fieldHandle.optional)
            )
            .mapToValuePartial((fieldHandle, value) -> {
                if (fieldHandle.serializer != Serializers.None.class) return Optional.of(NBTHandlerRegistry.getSpecialSerializer(fieldHandle.serializer).serialize(value));
                else if (value != null) return Optional.ofNullable(serializeField(value));
                return Optional.empty();
            })
            .mapKeys(FieldHandle::getName)
            .forKeyValue(nbt::setTag);
    }

    public static void readClassFromNBT(Object instance, NBTTagCompound nbt) {
        withFieldHandles(instance.getClass())
            .cross(fieldHandle -> StreamEx.of(nbt.getTag(fieldHandle.name))
                .nonNull()
            )
            .forKeyValue((fieldHandle, nbtValue) -> {
                if (fieldHandle.modifyExisting) {
                    Object value = fieldHandle.getFieldValue(instance);
                    modifyExistingField(fieldHandle.type, value, nbtValue);
                }
                else {
                    Object deserialized;
                    if (fieldHandle.deserializer != Serializers.None.class) {
                        deserialized = NBTHandlerRegistry.getSpecialDeserializer(fieldHandle.deserializer).deserialize(nbtValue, instance, fieldHandle.type);
                    }
                    else deserialized = deserializeField(nbtValue, instance, fieldHandle.type);

                    if (deserialized != null) fieldHandle.setFieldValue(instance, deserialized);
                }
            });
    }

    private static NBTBase serializeField(Object value) {
        return NBTHandlerRegistry.getSerializer(value).serialize(value);
    }

    private static Object deserializeField(NBTBase nbt, Object instance, Class<?> type) {
        return NBTHandlerRegistry.getDeserializer(type).deserialize(nbt, instance, type);
    }

    private static void modifyExistingField(Class<?> type, Object value, NBTBase nbt) {
        NBTHandlerRegistry.getModifyingDeserializer(type).modifyValue(value, nbt);
    }

    private static void checkForDuplicateField(String name, Class<?> cls) {
        boolean exists = withFieldHandles(cls)
            .map(FieldHandle::getName)
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
