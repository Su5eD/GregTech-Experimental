package mods.gregtechmod.util.nbt;

import com.mojang.authlib.GameProfile;
import mods.gregtechmod.objects.blocks.teblocks.computercube.ComputerCubeModules;
import mods.gregtechmod.objects.blocks.teblocks.computercube.IComputerCubeModule;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.util.ResourceLocation;

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
    
    public static NBTTagCompound serializeComputerCubeModule(IComputerCubeModule module) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("name", module.getName().toString());
        NBTTagCompound data = new NBTTagCompound();
        NBTSaveHandler.writeClassToNBT(module, data);
        tag.setTag("data", data);
        return tag;
    }
    
    public static IComputerCubeModule deserializeComputerCubeModule(NBTTagCompound nbt) {
        ResourceLocation name = new ResourceLocation(nbt.getString("name"));
        IComputerCubeModule module = ComputerCubeModules.getModule(name);
        NBTTagCompound data = nbt.getCompoundTag("data");
        NBTSaveHandler.readClassFromNBT(module, data);
        return module;
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
    
    static class EnumNBTSerializer implements INBTSerializer<Enum<?>, NBTTagString> {
        @Override
        public NBTTagString serialize(Enum<?> value) {
            return new NBTTagString(value.name());
        }

        @SuppressWarnings({"rawtypes", "unchecked"})
        @Override
        public Enum<?> deserialize(NBTTagString nbt, Object instance, Class cls) {
            String name = nbt.getString();
            return Enum.valueOf(cls, name);
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
