package mods.gregtechmod.util.nbt;

import com.mojang.authlib.GameProfile;
import mods.gregtechmod.objects.blocks.teblocks.computercube.IComputerCubeModule;
import mods.gregtechmod.util.LazyValue;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public final class NBTHandlerRegistry {
    private static final Map<Class<?>, LazyValue<INBTSerializer<Object, NBTBase>>> SERIALIZERS = new HashMap<>();
    private static final Collection<LazyValue<INBTSerializer<Object, NBTBase>>> SPECIAL_SERIALIZERS = new HashSet<>();

    private static final Map<Class<?>, LazyValue<INBTDeserializer<Object, NBTBase>>> DESERIALIZERS = new HashMap<>();
    private static final Map<Class<?>, LazyValue<INBTModifyingDeserializer<Object, NBTBase>>> MODIFYING_DESERIALIZERS = new HashMap<>();
    private static final Collection<LazyValue<INBTDeserializer<Object, NBTBase>>> SPECIAL_DESERIALIZERS = new HashSet<>();

    private NBTHandlerRegistry() {}

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
        addSimpleSerializer(IForgeRegistryEntry.class, Serializers::serializeIForgeRegistryEntry, Serializers::deserializeIForgeRegistryEntry);
        addSimpleSerializer(BlockPos.class, Serializers::serializeBlockPos, Serializers::deserializeBlockPos);
        addSerializer(List.class, Serializers.ListSerializer::new);

        addModifyingDeserializer(List.class, Serializers.ListModifyingDeserializer::new);

        addHandler(Enum.class, Serializers.EnumNBTSerializer::new);
        addHandler(IComputerCubeModule.class, Serializers.ComputerCubeModuleSerializer::new);

        addSpecialHandler(Serializers.ItemStackListNBTSerializer::new);
    }

    @SuppressWarnings("unchecked")
    public static <T, U extends NBTBase> void addSimpleSerializer(Class<T> clazz, INBTSerializer<T, U> serializer, Function<U, T> deserializer) {
        addSerializer(clazz, () -> serializer);
        addDeserializer(clazz, () -> (nbt, instance, cls) -> deserializer.apply((U) nbt));
    }

    @SuppressWarnings("unchecked")
    public static <T, U extends NBTBase> void addSimpleSerializer(Class<T> serializeClass, INBTSerializer<T, U> serializer, Class<T> deserializeClass, Function<U, T> deserializer) {
        addSerializer(serializeClass, () -> serializer);
        addDeserializer(deserializeClass, () -> (nbt, instance, cls) -> deserializer.apply((U) nbt));
    }

    public static <T, U extends NBTBase> void addHandler(Class<?> clazz, Supplier<INBTHandler<T, U>> handler) {
        addSerializer(clazz, handler);
        addDeserializer(clazz, handler);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T, U extends NBTBase> void addSerializer(Class<?> clazz, Supplier<? extends INBTSerializer<T, U>> serializer) {
        SERIALIZERS.put(clazz, new LazyValue(serializer));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T, U extends NBTBase> void addDeserializer(Class<?> clazz, Supplier<? extends INBTDeserializer<T, U>> serializer) {
        DESERIALIZERS.put(clazz, new LazyValue(serializer));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void addModifyingDeserializer(Class<?> clazz, Supplier<? extends INBTModifyingDeserializer<?, ? extends NBTBase>> deserializer) {
        MODIFYING_DESERIALIZERS.put(clazz, new LazyValue(deserializer));
    }

    public static void addSpecialHandler(Supplier<? extends INBTHandler<?, ? extends NBTBase>> handler) {
        addSpecialSerializer(handler);
        addSpecialDeserializer(handler);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void addSpecialSerializer(Supplier<? extends INBTSerializer<?, ? extends NBTBase>> serializer) {
        SPECIAL_SERIALIZERS.add(new LazyValue(serializer));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void addSpecialDeserializer(Supplier<? extends INBTDeserializer<?, ? extends NBTBase>> deserializer) {
        SPECIAL_DESERIALIZERS.add(new LazyValue(deserializer));
    }

    static INBTSerializer<Object, NBTBase> getSerializer(Object obj) {
        return getSerializer(obj.getClass());
    }

    static INBTSerializer<Object, NBTBase> getSerializer(Class<?> cls) {
        return getForClass("serializer", SERIALIZERS, cls);
    }

    static INBTDeserializer<Object, NBTBase> getDeserializer(Class<?> cls) {
        return getForClass("deserializer", DESERIALIZERS, cls);
    }

    static INBTModifyingDeserializer<Object, NBTBase> getModifyingDeserializer(Class<?> cls) {
        return getForClass("modifying deserializer", MODIFYING_DESERIALIZERS, cls);
    }

    static INBTSerializer<Object, NBTBase> getSpecialSerializer(Class<?> cls) {
        return getSpecial("serializer", SPECIAL_SERIALIZERS, cls);
    }

    static INBTDeserializer<Object, NBTBase> getSpecialDeserializer(Class<?> cls) {
        return getSpecial("deserializer", SPECIAL_DESERIALIZERS, cls);
    }

    private static <T> T getSpecial(String name, Collection<LazyValue<T>> collection, Class<?> cls) {
        return collection.stream()
            .map(LazyValue::get)
            .filter(obj -> obj.getClass() == cls)
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Can't find special " + name + " of type " + cls.getName()));
    }

    private static <T> T getForClass(String name, Map<Class<?>, LazyValue<T>> map, Class<?> cls) {
        return map.entrySet().stream()
            .filter(entry -> entry.getKey().isAssignableFrom(cls))
            .map(entry -> entry.getValue().get())
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Can't find " + name + " for class " + cls.getName()));
    }
}
