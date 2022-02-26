package dev.su5ed.gregtechmod.util.nbt;

import com.google.common.base.Preconditions;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.*;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.IForgeRegistryEntry;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.ClassUtils;

import java.util.*;
import java.util.function.Function;

public final class NBTHandlerRegistry {
    private static final Map<Class<?>, NBTSerializer<Object, Tag>> SERIALIZERS = new HashMap<>();
    private static final Collection<NBTSerializer<Object, Tag>> SPECIAL_SERIALIZERS = new HashSet<>();

    private static final Map<Class<?>, NBTDeserializer<Object, Tag, Object>> DESERIALIZERS = new HashMap<>();
    private static final Map<Class<?>, NBTModifyingDeserializer<Object, Tag>> MODIFYING_DESERIALIZERS = new HashMap<>();
    private static final Collection<NBTDeserializer<Object, Tag, Object>> SPECIAL_DESERIALIZERS = new HashSet<>();

    private NBTHandlerRegistry() {}

    static {
        addPrimitiveSerializer(byte.class, ByteTag::valueOf, ByteTag::getAsByte);
        addPrimitiveSerializer(short.class, ShortTag::valueOf, ShortTag::getAsShort);
        addPrimitiveSerializer(int.class, IntTag::valueOf, IntTag::getAsInt);
        addPrimitiveSerializer(long.class, LongTag::valueOf, LongTag::getAsLong);
        addPrimitiveSerializer(float.class, FloatTag::valueOf, FloatTag::getAsFloat);
        addPrimitiveSerializer(double.class, DoubleTag::valueOf, DoubleTag::getAsDouble);
        addPrimitiveSerializer(boolean.class, Serializers::serializeBoolean, Serializers::deserializeBoolean);
        addSimpleSerializer(byte[].class, ByteArrayTag::new, ByteArrayTag::getAsByteArray);
        addSimpleSerializer(int[].class, IntArrayTag::new, IntArrayTag::getAsIntArray);
        addSimpleSerializer(String.class, StringTag::valueOf, StringTag::getAsString);

        addSimpleSerializer(ItemStack.class, Serializers::serializeItemStack, ItemStack::of);
        addSimpleSerializer(GameProfile.class, Serializers::serializeGameProfile, NbtUtils::readGameProfile);
        addSimpleSerializer(IForgeRegistryEntry.class, Serializers::serializeIForgeRegistryEntry, Serializers::deserializeIForgeRegistryEntry);
        addSimpleSerializer(BlockPos.class, NbtUtils::writeBlockPos, NbtUtils::readBlockPos);
        addSerializer(List.class, Serializers.ListSerializer.INSTANCE);

        addModifyingDeserializer(List.class, Serializers.ListModifyingDeserializer.INSTANCE);

        addHandler(Enum.class, Serializers.EnumNBTSerializer.INSTANCE);

        addSpecialHandler(Serializers.ItemStackListNBTSerializer.INSTANCE);
    }
    
    @SuppressWarnings("unchecked")
    public static <T, U extends Tag> void addPrimitiveSerializer(Class<T> clazz, Function<T, U> serializer, Function<U, T> deserializer) {
        Preconditions.checkArgument(clazz.isPrimitive(), "Class must be of primitive type");
        
        addSimpleSerializer((Class<T>) ClassUtils.primitiveToWrapper(clazz), serializer, clazz, deserializer);
    }

    public static <T, U extends Tag> void addSimpleSerializer(Class<T> clazz, Function<T, U> serializer, Function<U, T> deserializer) {
        addSimpleSerializer(clazz, serializer, clazz, deserializer);
    }

    @SuppressWarnings("unchecked")
    public static <T, U extends Tag> void addSimpleSerializer(Class<T> serializeClass, Function<T, U> serializer, Class<T> deserializeClass, Function<U, T> deserializer) {
        NBTHandlerRegistry.<T, U>addSerializer(serializeClass, (value, target) -> serializer.apply(value));
        addDeserializer(deserializeClass, (tag, instance, cls) -> deserializer.apply((U) tag));
    }

    public static <T, U extends Tag, V> void addHandler(Class<?> clazz, NBTHandler<T, U, V> handler) {
        addSerializer(clazz, handler);
        addDeserializer(clazz, handler);
    }

    @SuppressWarnings({ "unchecked" })
    public static <T, U extends Tag> void addSerializer(Class<?> clazz, NBTSerializer<T, U> serializer) {
        SERIALIZERS.put(clazz, (NBTSerializer<Object, Tag>) serializer);
    }

    @SuppressWarnings({ "unchecked" })
    public static <T, U extends Tag, V> void addDeserializer(Class<?> clazz, NBTDeserializer<T, U, V> deserializer) {
        DESERIALIZERS.put(clazz, (NBTDeserializer<Object, Tag, Object>) deserializer);
    }

    @SuppressWarnings({ "unchecked" })
    public static void addModifyingDeserializer(Class<?> clazz, NBTModifyingDeserializer<?, ? extends Tag> deserializer) {
        MODIFYING_DESERIALIZERS.put(clazz, (NBTModifyingDeserializer<Object, Tag>) deserializer);
    }

    public static void addSpecialHandler(NBTHandler<?, ? extends Tag, ?> handler) {
        addSpecialSerializer(handler);
        addSpecialDeserializer(handler);
    }

    @SuppressWarnings({ "unchecked" })
    public static void addSpecialSerializer(NBTSerializer<?, ? extends Tag> serializer) {
        SPECIAL_SERIALIZERS.add((NBTSerializer<Object, Tag>) serializer);
    }

    @SuppressWarnings({ "unchecked" })
    public static void addSpecialDeserializer(NBTDeserializer<?, ? extends Tag, ?> deserializer) {
        SPECIAL_DESERIALIZERS.add((NBTDeserializer<Object, Tag, Object>) deserializer);
    }

    static NBTSerializer<Object, Tag> getSerializer(Object obj) {
        return getSerializer(obj.getClass());
    }

    static NBTSerializer<Object, Tag> getSerializer(Class<?> cls) {
        return getForClass("serializer", SERIALIZERS, cls);
    }

    static NBTDeserializer<Object, Tag, Object> getDeserializer(Class<?> cls) {
        return getForClass("deserializer", DESERIALIZERS, cls);
    }

    static NBTModifyingDeserializer<Object, Tag> getModifyingDeserializer(Class<?> cls) {
        return getForClass("modifying deserializer", MODIFYING_DESERIALIZERS, cls);
    }

    static NBTSerializer<Object, Tag> getSpecialSerializer(Class<?> cls) {
        return getSpecial("serializer", SPECIAL_SERIALIZERS, cls);
    }

    static NBTDeserializer<Object, Tag, Object> getSpecialDeserializer(Class<?> cls) {
        return getSpecial("deserializer", SPECIAL_DESERIALIZERS, cls);
    }

    private static <T> T getSpecial(String name, Collection<T> collection, Class<?> cls) {
        return StreamEx.of(collection)
            .findFirst(obj -> obj.getClass() == cls)
            .orElseThrow(() -> new RuntimeException("Can't find special " + name + " of type " + cls.getName()));
    }

    public static <T> T getForClass(String name, Map<Class<?>, T> map, Class<?> clazz) {
        return EntryStream.of(map)
            .filterKeys(cls -> cls.isAssignableFrom(clazz))
            .values()
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Can't find " + name + " for class " + clazz.getName()));
    }
}
