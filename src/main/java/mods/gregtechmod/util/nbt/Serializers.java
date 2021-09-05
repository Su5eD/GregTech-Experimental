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
    
    public static NBTBase serializeEnum(Enum<?> value) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("name", value.name());
        nbt.setString("type", value.getDeclaringClass().getName());
        return nbt;
    }
    
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Enum<?> deserializeEnum(NBTBase nbt) {
        if (nbt instanceof NBTTagCompound) { // Backwards error prevention / TODO Remove in next version
            String enumName = ((NBTTagCompound) nbt).getString("name");
            String type = ((NBTTagCompound) nbt).getString("type");
            try {
                Class enumClazz = Class.forName(type);
                return Enum.valueOf(enumClazz, enumName);
            } catch (ClassNotFoundException e) {
                GregTechMod.LOGGER.catching(e);
            }
        }
        return null;
    }
    
    public static NBTTagCompound serializeGameProfile(GameProfile profile) {
        return NBTUtil.writeGameProfile(new NBTTagCompound(), profile);
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
        public List<?> deserialize(NBTTagList nbt, Object instance) {
            List<Object> list = new ArrayList<>();
            for (NBTBase tag : nbt) {
                try {
                    String type = ((NBTTagCompound) tag).getString("type");
                    Class<?> clazz = Class.forName(type);
                    INBTSerializer<?, NBTBase> serializer = NBTSaveHandler.getSerializer(clazz);
                    if (serializer != null) {
                        NBTTagCompound value = ((NBTTagCompound) tag).getCompoundTag("value");
                        Object deserialized = serializer.deserialize(value, instance); 
                        list.add(deserialized);
                    }
                } catch (ClassNotFoundException e) {
                    GregTechMod.LOGGER.catching(e);
                }
            }
            return list;
        }
    }
    
    static class None implements INBTSerializer<Object, NBTBase> {
        @Override
        public NBTBase serialize(Object value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object deserialize(NBTBase nbt, Object instance) {
            throw new UnsupportedOperationException();
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
        public T deserialize(U nbt, Object instance) {
            return this.deserializer.apply(nbt);
        }
    }
}
