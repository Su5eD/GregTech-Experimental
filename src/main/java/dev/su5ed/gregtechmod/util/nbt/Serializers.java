package dev.su5ed.gregtechmod.util.nbt;

import com.mojang.authlib.GameProfile;
import dev.su5ed.gregtechmod.GregTechMod;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryManager;
import one.util.streamex.StreamEx;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public final class Serializers {
    
    private Serializers() {}

    public static ByteTag serializeBoolean(boolean bool) {
        return ByteTag.valueOf(bool ? (byte) 1 : 0);
    }

    public static boolean deserializeBoolean(ByteTag tag) {
        return tag.getAsByte() != 0;
    }

    public static CompoundTag serializeItemStack(ItemStack stack) {
        return stack.save(new CompoundTag());
    }

    public static CompoundTag serializeGameProfile(GameProfile profile) {
        return NbtUtils.writeGameProfile(new CompoundTag(), profile);
    }

    @SuppressWarnings("ConstantConditions")
    public static CompoundTag serializeIForgeRegistryEntry(IForgeRegistryEntry<?> entry) {
        CompoundTag tag = new CompoundTag();
        tag.putString("name", entry.getRegistryName().toString());
        tag.putString("type", entry.getRegistryType().getName());
        return tag;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static IForgeRegistryEntry<?> deserializeIForgeRegistryEntry(CompoundTag tag) {
        try {
            ResourceLocation name = new ResourceLocation(tag.getString("name"));
            Class type = Class.forName(tag.getString("type"));

            IForgeRegistry<?> registry = RegistryManager.ACTIVE.getRegistry(type);
            return registry.getValue(name);
        } catch (Exception e) {
            GregTechMod.LOGGER.catching(e);
            return null;
        }
    }

    public static class ListSerializer implements NBTSerializer<List<?>, ListTag> {

        @Override
        public ListTag serialize(List<?> value) {
            return StreamEx.of(value)
                .<Tag>map(NBTSaveHandler::writeClassToNBT)
                .toCollection(ListTag::new);
        }
    }

    public abstract static class ListDeserializer<T> implements NBTDeserializer<List<?>, ListTag, T> {
        private final BiFunction<T, Integer, ?> factory;

        protected ListDeserializer(BiFunction<T, Integer, ?> factory) {
            this.factory = factory;
        }

        @Override
        public List<?> deserialize(ListTag nbt, T instance, Class<?> cls) {
            List<? super Object> list = new ArrayList<>();

            for (int i = 0; i < nbt.size(); i++) {
                CompoundTag tag = nbt.getCompound(i);
                Object value = this.factory.apply(instance, i);
                NBTSaveHandler.readClassFromNBT(value, tag);
                list.add(value);
            }

            return list;
        }
    }

    public static class ListModifyingDeserializer implements NBTModifyingDeserializer<List<?>, ListTag> {

        @Override
        public void modifyValue(List<?> value, ListTag tag) {
            if (tag.getElementType() != Tag.TAG_COMPOUND) {
                throw new IllegalArgumentException("ListTag must be of type TAG_COMPOUND");
            }
            else if (value.size() != tag.size()) {
                GregTechMod.LOGGER.error("Found varying sizes for tag list {} and value {}", tag, value);
                throw new IllegalArgumentException("Varying sizes of existing and serialized value");
            }

            for (int i = 0; i < value.size(); i++) {
                NBTSaveHandler.readClassFromNBT(value.get(i), tag.getCompound(i));
            }
        }
    }

    public static class ItemStackListNBTSerializer implements NBTHandler<List<ItemStack>, ListTag, Object> {
        @Override
        public ListTag serialize(List<ItemStack> value) {
            return StreamEx.of(value)
                .<Tag>map(stack -> stack.save(new CompoundTag()))
                .toCollection(ListTag::new);
        }
        
        @Override
        public List<ItemStack> deserialize(ListTag tag, Object instance, Class<?> cls) {
            return StreamEx.of(tag)
                .select(CompoundTag.class)
                .map(ItemStack::of)
                .toImmutableList();
        }
    }

    static class EnumNBTSerializer implements NBTHandler<Enum<?>, StringTag, Object> {
        @Override
        public StringTag serialize(Enum<?> value) {
            return StringTag.valueOf(value.name());
        }

        @SuppressWarnings({ "rawtypes", "unchecked" })
        @Override
        public Enum<?> deserialize(StringTag tag, Object instance, Class cls) {
            String name = tag.getAsString();
            return Enum.valueOf(cls, name);
        }
    }

    static class None implements NBTHandler<Object, Tag, Object> {
        @Override
        public Tag serialize(Object value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object deserialize(Tag tag, Object instance, Class<?> cls) {
            throw new UnsupportedOperationException();
        }
    }
}
