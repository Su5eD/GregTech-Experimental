package mods.gregtechmod.util.nbt;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class NBTSaveHandler {
    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
    private static final Map<Class<?>, List<FieldHandle>> HANDLES = new HashMap<>();
    
    public static void initClass(Class<?> cls) { // TODO Parallel execution?
        withParents(cls, clazz -> !HANDLES.containsKey(clazz), clazz -> {
            List<FieldHandle> fieldHandles = Arrays.stream(clazz.getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(NBTPersistent.class))
                    .map(field -> {
                        try {
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
                            
                            checkForDuplicateField(name, clazz);
                            return new FieldHandle(name, field.getType(), serializer, deserializer, persistent.include().predicate, modifyExisting, LOOKUP.unreflectGetter(field), LOOKUP.unreflectSetter(field));
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException("Unable to create handle for field " + field.getName(), e);
                        }
                    })
                    .collect(Collectors.toList());
            if (!fieldHandles.isEmpty()) HANDLES.put(clazz, fieldHandles);
        });
    }

    public static void writeClassToNBT(Object instance, NBTTagCompound nbt) {
        withFieldHandles(instance.getClass(), fieldHandle -> {
            Object value = fieldHandle.getFieldValue(instance);
            if (fieldHandle.optional.test(value)) {
                NBTBase serialized;
                if (fieldHandle.serializer != Serializers.None.class) {
                    serialized = NBTHandlerRegistry.getSpecialSerializer(fieldHandle.serializer).serialize(value);
                }
                else serialized = serializeField(value);

                if (serialized != null) nbt.setTag(fieldHandle.name, serialized);
            }
        });
    }
    
    public static void readClassFromNBT(Object instance, NBTTagCompound nbt) {
        withFieldHandles(instance.getClass(), fieldHandle -> {
            if (nbt.hasKey(fieldHandle.name)) {
                NBTBase nbtValue = nbt.getTag(fieldHandle.name);

                if (fieldHandle.modifyExisting) {
                    Object value = fieldHandle.getFieldValue(instance);
                    modifyExistingField(fieldHandle.type, value, nbtValue);
                } else {
                    Object deserialized;
                    if (fieldHandle.deserializer != Serializers.None.class) {
                        deserialized = NBTHandlerRegistry.getSpecialDeserializer(fieldHandle.deserializer).deserialize(nbtValue, instance, fieldHandle.type);
                    }
                    else deserialized = deserializeField(nbtValue, instance, fieldHandle.type);

                    if (deserialized != null) fieldHandle.setFieldValue(instance, deserialized);
                }
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
        withFieldHandles(cls, fieldHandle -> {
            if (name.equals(fieldHandle.name)) throw new RuntimeException("Duplicate field " + formatFieldName(cls, name));
        });
    }
    
    private static void withFieldHandles(Class<?> clazz, Consumer<FieldHandle> consumer) {
        withParents(clazz, HANDLES::containsKey, cls -> {
            List<FieldHandle> fieldHandles = HANDLES.get(cls);
            fieldHandles.forEach(consumer);
        });
    }
    
    private static void withParents(Class<?> clazz, Predicate<Class<?>> filter, Consumer<Class<?>> runnable) {
        Class<?> parent = clazz.getSuperclass();
        if (parent != Object.class) withParents(parent, filter, runnable);
                
        if (filter.test(clazz)) runnable.accept(clazz);
    }
    
    private static String formatFieldName(Class<?> clazz, String field) {
        return clazz.getName() + "#" + field;
    }
}
