package mods.gregtechmod.util.nbt;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.authlib.GameProfile;
import ic2.core.block.invslot.InvSlot;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.objects.blocks.teblocks.computercube.IComputerCubeModule;
import mods.gregtechmod.util.LazyValue;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public final class NBTSaveHandler {
    private static final Class<NBTPersistent> ANNOTATION_CLASS = NBTPersistent.class;
    private static final Map<Class<?>, List<FieldHandle>> HANDLES = new HashMap<>();
    private static final Map<Class<?>, LazyValue<INBTSerializer<Object, NBTBase>>> SERIALIZERS = new HashMap<>();
    private static final Multimap<Class<?>, LazyValue<INBTSerializer<Object, NBTBase>>> SPECIAL_SERIALIZERS = HashMultimap.create();
    
    static {
        addSimpleSerializer(Byte.class, NBTTagByte::new, byte.class, NBTTagByte::getByte);
        addSimpleSerializer(Short.class, NBTTagShort::new, short.class, NBTTagShort::getShort);
        addSimpleSerializer(Integer.class, NBTTagInt::new, int.class, NBTTagInt::getInt);
        addSimpleSerializer(Long.class, NBTTagLong::new, long.class, NBTTagLong::getLong);
        addSimpleSerializer(Float.class, NBTTagFloat::new, float.class, NBTTagFloat::getFloat);
        addSimpleSerializer(Double.class, NBTTagDouble::new, double.class, NBTTagDouble::getDouble);
        addSimpleSerializer(Boolean.class, Serializers::serializeBoolean, boolean.class, Serializers::deserializeBoolean);
        addSimpleSerializer(byte[].class, NBTTagByteArray::new, NBTTagByteArray::getByteArray);
        addSimpleSerializer(int[].class, NBTTagIntArray::new, NBTTagIntArray::getIntArray);
        addSimpleSerializer(String.class, NBTTagString::new, NBTTagString::getString);
        
        addSimpleSerializer(ItemStack.class, Serializers::serializeItemStack, ItemStack::new);
        addSimpleSerializer(GameProfile.class, Serializers::serializeGameProfile, NBTUtil::readGameProfileFromNBT);
        addSimpleSerializer(InvSlot.class, Serializers::serializeInvSlot, Serializers::deserializeInvSlot);
        addSimpleSerializer(IForgeRegistryEntry.class, Serializers::serializeIForgeRegistryEntry, Serializers::deserializeIForgeRegistryEntry);
        addSimpleSerializer(BlockPos.class, Serializers::serializeBlockPos, Serializers::deserializeBlockPos);
        addSerializer(Enum.class, Serializers.EnumNBTSerializer::new);
        addSerializer(IComputerCubeModule.class, Serializers.ComputerCubeModuleSerializer::new);
        
        addSpecialSerializer(List.class, Serializers.ItemStackListNBTSerializer::new);
    }
    
    public static <T, U extends NBTBase> void addSimpleSerializer(Class<T> clazz, Function<T, U> serializer, Function<U, T> deserializer) {
        Supplier<INBTSerializer<T, U>> supplier = () -> new Serializers.SimpleNBTSerializer<>(serializer, deserializer);
        addSerializer(clazz, supplier);
    }
    
    public static <T, U extends NBTBase> void addSimpleSerializer(Class<T> serializeClass, Function<T, U> serializer, Class<T> deserializeClass, Function<U, T> deserializer) {
        Supplier<INBTSerializer<T, U>> supplier = () -> new Serializers.SimpleNBTSerializer<>(serializer, deserializer);
        addSerializer(serializeClass, supplier);
        addSerializer(deserializeClass, supplier);
    }
    
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T, U extends NBTBase> void addSerializer(Class<?> clazz, Supplier<INBTSerializer<T, U>> serializer) {
        SERIALIZERS.put(clazz, new LazyValue(serializer));
    }
    
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void addSpecialSerializer(Class<?> clazz, Supplier<INBTSerializer<?, ? extends NBTBase>> serializer) {
        SPECIAL_SERIALIZERS.put(clazz, new LazyValue(serializer));
    }
    
    public static void initClass(Class<?> cls) {
        withParents(cls, clazz -> !HANDLES.containsKey(clazz), clazz -> {
            MethodHandles.Lookup lookup = MethodHandles.lookup();
                        
            List<FieldHandle> fieldHandles = Arrays.stream(clazz.getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(ANNOTATION_CLASS))
                    .map(field -> {
                        try {
                            field.setAccessible(true);
                            NBTPersistent persistent = field.getAnnotation(ANNOTATION_CLASS);
                            String annotatedName = persistent.name();
                            String name = !annotatedName.isEmpty() ? annotatedName : field.getName();
                            checkForDuplicateField(name, clazz);
                            return new FieldHandle(name, field.getType(), persistent.using(), persistent.include().predicate, lookup.unreflectGetter(field), lookup.unreflectSetter(field));
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException("Unable to unreflect handle for field " + field.getName(), e);
                        }
                    })
                    .collect(Collectors.toList());
            if (!fieldHandles.isEmpty()) HANDLES.put(clazz, fieldHandles);
        });
    }

    public static void writeClassToNBT(Object instance, NBTTagCompound nbt) {
        withParents(instance.getClass(), HANDLES::containsKey, clazz -> {
            List<FieldHandle> fieldHandles = HANDLES.get(clazz);
            if (fieldHandles != null) {
                fieldHandles.forEach(fieldHandle -> {
                    Object value = fieldHandle.getFieldValue(instance);
                    if (fieldHandle.optional.test(value)) {
                        NBTBase serialized;
                        if (fieldHandle.serializer != Serializers.None.class) serialized = getSpecialSerializer(fieldHandle.type, fieldHandle.serializer).serialize(value);
                        else serialized = serializeField(fieldHandle.name, value);
                        
                        if (serialized != null) nbt.setTag(fieldHandle.name, serialized);
                    }
                });
            }
        });
    }
    
    public static void readClassFromNBT(Object instance, NBTTagCompound nbt) {
        withParents(instance.getClass(), HANDLES::containsKey, clazz -> {
            List<FieldHandle> fieldHandles = HANDLES.get(clazz);
            if (fieldHandles != null) {
                fieldHandles.forEach(fieldHandle -> {
                    if (nbt.hasKey(fieldHandle.name)) {
                        NBTBase nbtValue = nbt.getTag(fieldHandle.name);
                        Object value;
                        if (fieldHandle.serializer != Serializers.None.class) value = getSpecialSerializer(fieldHandle.type, fieldHandle.serializer).deserialize(nbtValue, instance, fieldHandle.type);
                        else value = deserializeField(nbtValue, fieldHandle.name, instance, fieldHandle.type);
                        
                        if (value != null) fieldHandle.setFieldValue(instance, value);
                    }
                });
            }
        });
    }

    private static NBTBase serializeField(String name, Object value) {
        INBTSerializer<Object, NBTBase> serializer = getSerializer(value);
        if (serializer != null) return serializer.serialize(value);

        GregTechMod.LOGGER.error("Failed to serialize field " + name + " of type " + value.getClass().getName());
        return null;
    }
    
    private static Object deserializeField(NBTBase nbt, String name, Object instance, Class<?> type) {
        for (Map.Entry<Class<?>, LazyValue<INBTSerializer<Object, NBTBase>>> entry : SERIALIZERS.entrySet()) {
            Class<?> clazz = entry.getKey();
            if (clazz.isAssignableFrom(type)) {
                return entry.getValue().get().deserialize(nbt, instance, type);
            }
        }
        
        GregTechMod.LOGGER.error("Failed to deserialize field " + name + " of type " + type.getName());
        return null;
    }
    
    private static void checkForDuplicateField(String name, Class<?> cls) {
        withParents(cls, HANDLES::containsKey, clazz -> {
            List<FieldHandle> fieldHandles = HANDLES.get(clazz);
            boolean exists = fieldHandles.stream()
                    .map(FieldHandle::getName)
                    .anyMatch(name::equals);
            if (exists) throw new RuntimeException("Duplicate field " + formatFieldName(cls, name) + " in class " + clazz.getName());
        });
    }
    
    static INBTSerializer<Object, NBTBase> getSerializer(Object obj) {
        return getSerializer(obj.getClass());
    }
    
    static INBTSerializer<Object, NBTBase> getSerializer(Class<?> cls) {
        for (Map.Entry<Class<?>, LazyValue<INBTSerializer<Object, NBTBase>>> entry : SERIALIZERS.entrySet()) {
            Class<?> clazz = entry.getKey();
            if (clazz.isAssignableFrom(cls)) {
                return entry.getValue().get();
            }
        }
        return null;
    }

    private static INBTSerializer<Object, NBTBase> getSpecialSerializer(Class<?> clazz, Class<?> serializerClazz) {
        return SPECIAL_SERIALIZERS.get(clazz).stream()
                .map(LazyValue::get)
                .filter(serializer -> serializer.getClass() == serializerClazz)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Can't find serializer of type " + serializerClazz.getName() + " for class " + clazz.getName()));
    }
    
    private static String formatFieldName(Class<?> clazz, String field) {
        return clazz.getName() + "#" + field;
    }
    
    private static void withParents(Class<?> clazz, Predicate<Class<?>> filter, Consumer<Class<?>> runnable) {
        Class<?> parent = clazz.getSuperclass();
        if (parent != Object.class) withParents(parent, filter, runnable);
                
        if (filter.test(clazz)) runnable.accept(clazz);
    }
}
