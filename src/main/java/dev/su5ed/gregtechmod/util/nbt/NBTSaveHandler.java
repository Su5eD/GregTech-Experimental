package dev.su5ed.gregtechmod.util.nbt;

import dev.su5ed.gregtechmod.api.util.NBTTarget;
import dev.su5ed.gregtechmod.util.Try;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import one.util.streamex.StreamEx;

import javax.annotation.Nullable;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

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
                        String customName = persistent.name();
                        String name = !customName.isEmpty() ? customName : field.getName();
                        boolean modifyExisting = field.isAnnotationPresent(ModifyExisting.class);
                        if (!modifyExisting && Modifier.isFinal(field.getModifiers())) {
                            throw new IllegalArgumentException("Cannot register deserializer for final field " + formatFieldName(cls, name));
                        }

                        Class<? extends NBTHandler<?, ?, ?>> handler = persistent.handler();
                        boolean customHandler = handler != Serializers.None.class;
                        Class<? extends NBTSerializer<?, ?>> serializer = customHandler ? handler : persistent.serializer();
                        Class<? extends NBTDeserializer<?, ?, ?>> deserializer = customHandler ? handler : persistent.deserializer();
                        VarHandle handle = MethodHandles.privateLookupIn(cls, LOOKUP).unreflectVarHandle(field);

                        checkForDuplicateField(name, cls);
                        return new FieldHandle(persistent.target(), name, field.getType(), handle, serializer, deserializer, persistent.include().predicate, modifyExisting);
                    })
                    .catching(field -> "Unable to create handle for field " + field.getName()))
                .toImmutableList()
            )
            .forKeyValue(HANDLES::put);
    }
    
    public static CompoundTag writeClassToNBT(Object instance) {
        return writeClassToNBT(instance, NBTTarget.SAVE);
    }
    
    public static CompoundTag writeClassToNBT(Object instance, NBTTarget target) {
        return serializeFields(instance, target, getFieldHandles(instance.getClass()));
    }
    
    public static void readClassFromNBT(Object instance, CompoundTag nbt) {
        readClassFromNBT(instance, nbt, true);
    }

    public static void readClassFromNBT(Object instance, CompoundTag nbt, boolean notifyListeners) {
        getFieldHandles(instance.getClass())
            .cross(handle -> StreamEx.of(nbt.get(handle.getName())).nonNull())
            .forKeyValue((handle, tag) -> {
                if (handle.modifyExisting) {
                    handle.getValue(instance).ifPresent(value -> modifyExistingField(handle.type, value, tag));
                }
                else {
                    deserializeField(handle, tag, instance, notifyListeners).ifPresent(obj -> {
                        handle.setValue(instance, obj);
                        if (notifyListeners && instance instanceof FieldUpdateListener listener) {
                            listener.onFieldUpdate(handle.name);
                        }
                    });
                }
            });
    }

    public static CompoundTag serializeFields(Object instance, NBTTarget target, FieldHandle... fields) {
        return serializeFields(instance, target, StreamEx.of(fields));
    }

    public static CompoundTag serializeFields(Object instance, NBTTarget target, StreamEx<FieldHandle> fields) {
        CompoundTag compound = new CompoundTag();
        fields
            .filter(field -> field.target.accepts(target))
            .mapToEntry(FieldHandle::getName, field -> serializeField(field, instance))
            .nonNullValues()
            .forKeyValue(compound::put);
        return compound;
    }

    @Nullable
    private static Tag serializeField(FieldHandle field, Object instance) {
        return field.getValue(instance)
            .map(value -> serializeFieldValue(field, value))
            .orElse(null);
    }

    @Nullable
    private static Tag serializeFieldValue(FieldHandle field, Object value) {
        NBTSerializer<Object, Tag> serializer;
        if (field.serializer != Serializers.None.class) serializer = NBTHandlerRegistry.getSpecialSerializer(field.serializer);
        else if (isClassRegistered(field.type)) return writeClassToNBT(value, field.target);
        else serializer = NBTHandlerRegistry.getSerializer(value.getClass());
        
        return serializer.serialize(value, field.target);
    }

    @Nullable
    private static Optional<Object> deserializeField(FieldHandle field, Tag tag, Object instance, boolean notifyListeners) {
        NBTDeserializer<Object, Tag, Object> deserializer;
        if (field.deserializer != Serializers.None.class) deserializer = NBTHandlerRegistry.getSpecialDeserializer(field.deserializer);
        else if (isClassRegistered(field.type) && tag instanceof CompoundTag compound) {
            field.getValue(instance).ifPresent(obj -> readClassFromNBT(obj, compound, notifyListeners));
            return Optional.empty();
        }
        else deserializer = NBTHandlerRegistry.getDeserializer(field.type);
        
        return Optional.ofNullable(deserializer.deserialize(tag, instance, field.type));
    }
    
    public static boolean isClassRegistered(Class<?> type) {
        return HANDLES.containsKey(type);
    }

    private static void modifyExistingField(Class<?> type, Object value, Tag tag) {
        NBTHandlerRegistry.getModifyingDeserializer(type).modifyValue(value, tag);
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
        return StreamEx.<Class<?>>iterate(clazz, Objects::nonNull, Class::getSuperclass)
            .without(Object.class);
    }

    public static String formatFieldName(Class<?> clazz, String field) {
        return clazz.getName() + "#" + field;
    }
}
