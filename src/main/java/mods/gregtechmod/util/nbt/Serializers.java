package mods.gregtechmod.util.nbt;

import com.mojang.authlib.GameProfile;
import ic2.core.block.invslot.InvSlot;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.objects.blocks.teblocks.computercube.ComputerCubeModules;
import mods.gregtechmod.objects.blocks.teblocks.computercube.IComputerCubeModule;
import mods.gregtechmod.objects.blocks.teblocks.computercube.TileEntityComputerCube;
import mods.gregtechmod.objects.blocks.teblocks.computercube.TileEntityComputerCube.ComputerCubeModuleComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

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
    
    public static NBTTagCompound serializeBlockPos(BlockPos vec) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("x", vec.getX());
        nbt.setInteger("y", vec.getY());
        nbt.setInteger("z", vec.getZ());
        return nbt;
    }
    
    public static BlockPos deserializeBlockPos(NBTTagCompound nbt) {
        int x = nbt.getInteger("x");
        int y = nbt.getInteger("y");
        int z = nbt.getInteger("z");
        return new BlockPos(x, y, z);
    }
    
    @SuppressWarnings("ConstantConditions")
    public static NBTTagCompound serializeIForgeRegistryEntry(IForgeRegistryEntry<?> entry) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("name", entry.getRegistryName().toString());
        nbt.setString("type", entry.getRegistryType().getName());
        return nbt;
    }
    
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static IForgeRegistryEntry<?> deserializeIForgeRegistryEntry(NBTTagCompound nbt) {
        try {
            ResourceLocation name = new ResourceLocation(nbt.getString("name"));
            Class type = Class.forName(nbt.getString("type"));

            IForgeRegistry<?> registry = GameRegistry.findRegistry(type);
            return registry.getValue(name);
        } catch (Exception e) {
            GregTechMod.LOGGER.catching(e);
            return null;
        }
    }
    
    public static NBTTagCompound serializeInvSlot(InvSlot slot) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("size", slot.size());
        slot.writeToNbt(nbt);
        return nbt;
    }
    
    public static InvSlot deserializeInvSlot(NBTTagCompound nbt) {
        int size = nbt.getInteger("size");
        InvSlot slot = new InvSlot(size);
        slot.readFromNbt(nbt);
        return slot;
    }
    
    static class ComputerCubeModuleSerializer implements INBTSerializer<IComputerCubeModule, NBTTagCompound> {
        @Override
        public NBTTagCompound serialize(IComputerCubeModule value) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("name", value.getName().toString());
            NBTTagCompound data = new NBTTagCompound();
            NBTSaveHandler.writeClassToNBT(value, data);
            tag.setTag("data", data);
            return tag;
        }

        @Override
        public IComputerCubeModule deserialize(NBTTagCompound nbt, Object instance, Class<?> cls) {
            ResourceLocation name = new ResourceLocation(nbt.getString("name"));
            IComputerCubeModule module = ComputerCubeModules.getModule(name, (TileEntityComputerCube) ((ComputerCubeModuleComponent) instance).getParent());
            NBTTagCompound data = nbt.getCompoundTag("data");
            NBTSaveHandler.readClassFromNBT(module, data);
            return module;
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
