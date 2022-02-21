package dev.su5ed.gregtechmod.util.nbt;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.*;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.IForgeRegistryEntry;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;

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
        addSimpleSerializer(Byte.class, ByteTag::valueOf, byte.class, ByteTag::getAsByte);
        addSimpleSerializer(Short.class, ShortTag::valueOf, short.class, ShortTag::getAsShort);
        addSimpleSerializer(Integer.class, IntTag::valueOf, int.class, IntTag::getAsInt);
        addSimpleSerializer(Long.class, LongTag::valueOf, long.class, LongTag::getAsLong);
        addSimpleSerializer(Float.class, FloatTag::valueOf, float.class, FloatTag::getAsFloat);
        addSimpleSerializer(Double.class, DoubleTag::valueOf, double.class, DoubleTag::getAsDouble);
        addSimpleSerializer(Boolean.class, Serializers::serializeBoolean, boolean.class, Serializers::deserializeBoolean);
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
    public static <T, U extends Tag> void addSimpleSerializer(Class<T> clazz, NBTSerializer<T, U> serializer, Function<U, T> deserializer) {
        addSerializer(clazz, serializer);
        addDeserializer(clazz, (nbt, instance, cls) -> deserializer.apply((U) nbt));
    }

    @SuppressWarnings("unchecked")
    public static <T, U extends Tag> void addSimpleSerializer(Class<T> serializeClass, NBTSerializer<T, U> serializer, Class<T> deserializeClass, Function<U, T> deserializer) {
        addSerializer(serializeClass, serializer);
        addDeserializer(deserializeClass, (nbt, instance, cls) -> deserializer.apply((U) nbt));
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

    public static void addSpecialHandler(NBTHandler<?, ? extends Tag, Object> handler) {
        addSpecialSerializer(handler);
        addSpecialDeserializer(handler);
    }

    @SuppressWarnings({ "unchecked" })
    public static void addSpecialSerializer(NBTSerializer<?, ? extends Tag> serializer) {
        SPECIAL_SERIALIZERS.add((NBTSerializer<Object, Tag>) serializer);
    }

    @SuppressWarnings({ "unchecked" })
    public static void addSpecialDeserializer(NBTDeserializer<?, ? extends Tag, Object> deserializer) {
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
