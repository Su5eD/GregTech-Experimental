package dev.su5ed.gregtechmod.util.nbt;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.*;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.IForgeRegistryEntry;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public final class NBTHandlerRegistry {
    private static final Map<Class<?>, Lazy<NBTSerializer<Object, Tag>>> SERIALIZERS = new HashMap<>();
    private static final Collection<Lazy<NBTSerializer<Object, Tag>>> SPECIAL_SERIALIZERS = new HashSet<>();

    private static final Map<Class<?>, Lazy<NBTDeserializer<Object, Tag>>> DESERIALIZERS = new HashMap<>();
    private static final Map<Class<?>, Lazy<NBTModifyingDeserializer<Object, Tag>>> MODIFYING_DESERIALIZERS = new HashMap<>();
    private static final Collection<Lazy<NBTDeserializer<Object, Tag>>> SPECIAL_DESERIALIZERS = new HashSet<>();

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
        addSerializer(List.class, Serializers.ListSerializer::new);

        addModifyingDeserializer(List.class, Serializers.ListModifyingDeserializer::new);

        addHandler(Enum.class, Serializers.EnumNBTSerializer::new);

        addSpecialHandler(Serializers.ItemStackListNBTSerializer::new);
    }

    @SuppressWarnings("unchecked")
    public static <T, U extends Tag> void addSimpleSerializer(Class<T> clazz, NBTSerializer<T, U> serializer, Function<U, T> deserializer) {
        addSerializer(clazz, () -> serializer);
        addDeserializer(clazz, () -> (nbt, instance, cls) -> deserializer.apply((U) nbt));
    }

    @SuppressWarnings("unchecked")
    public static <T, U extends Tag> void addSimpleSerializer(Class<T> serializeClass, NBTSerializer<T, U> serializer, Class<T> deserializeClass, Function<U, T> deserializer) {
        addSerializer(serializeClass, () -> serializer);
        addDeserializer(deserializeClass, () -> (nbt, instance, cls) -> deserializer.apply((U) nbt));
    }

    public static <T, U extends Tag> void addHandler(Class<?> clazz, Supplier<NBTHandler<T, U>> handler) {
        addSerializer(clazz, handler);
        addDeserializer(clazz, handler);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T, U extends Tag> void addSerializer(Class<?> clazz, Supplier<? extends NBTSerializer<T, U>> serializer) {
        SERIALIZERS.put(clazz, (Lazy) Lazy.of(serializer));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T, U extends Tag> void addDeserializer(Class<?> clazz, Supplier<? extends NBTDeserializer<T, U>> serializer) {
        DESERIALIZERS.put(clazz, (Lazy) Lazy.of(serializer));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void addModifyingDeserializer(Class<?> clazz, Supplier<? extends NBTModifyingDeserializer<?, ? extends Tag>> deserializer) {
        MODIFYING_DESERIALIZERS.put(clazz, (Lazy) Lazy.of(deserializer));
    }

    public static void addSpecialHandler(Supplier<? extends NBTHandler<?, ? extends Tag>> handler) {
        addSpecialSerializer(handler);
        addSpecialDeserializer(handler);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void addSpecialSerializer(Supplier<? extends NBTSerializer<?, ? extends Tag>> serializer) {
        SPECIAL_SERIALIZERS.add((Lazy) Lazy.of(serializer));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void addSpecialDeserializer(Supplier<? extends NBTDeserializer<?, ? extends Tag>> deserializer) {
        SPECIAL_DESERIALIZERS.add((Lazy) Lazy.of(deserializer));
    }

    static NBTSerializer<Object, Tag> getSerializer(Object obj) {
        return getSerializer(obj.getClass());
    }

    static NBTSerializer<Object, Tag> getSerializer(Class<?> cls) {
        return getForClass("serializer", SERIALIZERS, cls);
    }

    static NBTDeserializer<Object, Tag> getDeserializer(Class<?> cls) {
        return getForClass("deserializer", DESERIALIZERS, cls);
    }

    static NBTModifyingDeserializer<Object, Tag> getModifyingDeserializer(Class<?> cls) {
        return getForClass("modifying deserializer", MODIFYING_DESERIALIZERS, cls);
    }

    static NBTSerializer<Object, Tag> getSpecialSerializer(Class<?> cls) {
        return getSpecial("serializer", SPECIAL_SERIALIZERS, cls);
    }

    static NBTDeserializer<Object, Tag> getSpecialDeserializer(Class<?> cls) {
        return getSpecial("deserializer", SPECIAL_DESERIALIZERS, cls);
    }

    private static <T> T getSpecial(String name, Collection<Lazy<T>> collection, Class<?> cls) {
        return StreamEx.of(collection)
            .map(Supplier::get)
            .findFirst(obj -> obj.getClass() == cls)
            .orElseThrow(() -> new RuntimeException("Can't find special " + name + " of type " + cls.getName()));
    }

    private static <T> T getForClass(String name, Map<Class<?>, Lazy<T>> map, Class<?> cls) {
        return EntryStream.of(map)
            .filterKeys(clazz -> clazz.isAssignableFrom(cls))
            .mapKeyValue((clazz, lazy) -> lazy.get())
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Can't find " + name + " for class " + cls.getName()));
    }
}
