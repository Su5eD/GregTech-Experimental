package mods.gregtechmod.util.nbt;

import com.mojang.authlib.GameProfile;
import mods.gregtechmod.core.GregTechMod;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public final class Serializers {
    
    public static NBTTagByte serializeBoolean(boolean bool) {
        return new NBTTagByte(bool ? (byte) 1 : 0);
    }
    
    public static boolean deserializeBoolean(NBTTagByte nbt) {
        return nbt.getByte() != 0;
    }
    
    public static NBTTagCompound serializeItemStack(ItemStack stack) {
        return stack.writeToNBT(new NBTTagCompound());
    }
    
    public static NBTTagCompound serializeGameProfile(GameProfile profile) {
        return NBTUtil.writeGameProfile(new NBTTagCompound(), profile);
    }
    
    static class EnumNBTSerializer implements INBTSerializer<Enum<?>, NBTBase> {
        @Override
        public NBTBase serialize(Enum<?> value) {
            return new NBTTagString(value.name());
        }

        @SuppressWarnings({"rawtypes", "unchecked"})
        @Override
        public Enum<?> deserialize(NBTBase nbt, Object instance, Class cls) {
            if (nbt instanceof NBTTagString) {
                String name = ((NBTTagString) nbt).getString();
                return Enum.valueOf(cls, name);
            }
            else if (nbt instanceof NBTTagInt) {
                int index = ((NBTTagInt) nbt).getInt();
                return (Enum<?>) cls.getEnumConstants()[index];
            }
            return null;
        }
    }
    
    public static class ItemStackListNBTSerializer implements INBTSerializer<List<ItemStack>, NBTTagList> {
        @Override
        public NBTTagList serialize(List<ItemStack> value) {
            NBTTagList list = new NBTTagList();
            value.stream()
                    .map(stack -> stack.writeToNBT(new NBTTagCompound()))
                    .forEach(list::appendTag);
            return list;
        }

        @Override
        public List<ItemStack> deserialize(NBTTagList nbt, Object instance, Class<?> cls) {
            List<ItemStack> list = new ArrayList<>();
            nbt.forEach(tag -> list.add(new ItemStack((NBTTagCompound) tag)));
            return list;
        }
    }
    
    static class ListNBTSerializer implements INBTSerializer<List<?>, NBTTagList> {
        @Override
        public NBTTagList serialize(List<?> list) {
            NBTTagList tagList = new NBTTagList();
            for (Object obj : list) {
                INBTSerializer<Object, ?> serializer = NBTSaveHandler.getSerializer(obj);
                if (serializer != null) {
                    NBTTagCompound tag = new NBTTagCompound();
                    tag.setString("type", obj.getClass().getName());
                    tag.setTag("value", serializer.serialize(obj));
                    tagList.appendTag(tag);
                }
            }
            return tagList;
        }

        @Override
        public List<?> deserialize(NBTTagList nbt, Object instance, Class<?> cls) {
            List<Object> list = new ArrayList<>();
            for (NBTBase tag : nbt) {
                try {
                    String type = ((NBTTagCompound) tag).getString("type");
                    Class<?> clazz = Class.forName(type);
                    INBTSerializer<?, NBTBase> serializer = NBTSaveHandler.getSerializer(clazz);
                    if (serializer != null) {
                        NBTTagCompound value = ((NBTTagCompound) tag).getCompoundTag("value");
                        Object deserialized = serializer.deserialize(value, instance, clazz); 
                        list.add(deserialized);
                    }
                } catch (ClassNotFoundException e) {
                    GregTechMod.LOGGER.catching(e);
                }
            }
            return list;
        }
    }
    
    static class SimpleNBTSerializer<T, U extends NBTBase> implements INBTSerializer<T, U> {
        private final Function<T, U> serializer;
        private final Function<U, T> deserializer;

        public SimpleNBTSerializer(Function<T, U> serializer, Function<U, T> deserializer) {
            this.serializer = serializer;
            this.deserializer = deserializer;
        }

        @Override
        public U serialize(T value) {
            return this.serializer.apply(value);
        }

        @Override
        public T deserialize(U nbt, Object instance, Class<?> cls) {
            return this.deserializer.apply(nbt);
        }
    }
    
    static class None implements INBTSerializer<Object, NBTBase> {
        @Override
        public NBTBase serialize(Object value) {
            throw new UnsupportedOperationException();
        }
    
        @Override
        public Object deserialize(NBTBase nbt, Object instance, Class<?> cls) {
            throw new UnsupportedOperationException();
        }
    }
}
