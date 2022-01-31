package mods.gregtechmod.util.nbt;

import com.mojang.authlib.GameProfile;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.objects.blocks.teblocks.computercube.ComputerCubeModules;
import mods.gregtechmod.objects.blocks.teblocks.computercube.IComputerCubeModule;
import mods.gregtechmod.objects.blocks.teblocks.computercube.TileEntityComputerCube;
import mods.gregtechmod.objects.blocks.teblocks.computercube.TileEntityComputerCube.ComputerCubeModuleComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

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
    
    static class ComputerCubeModuleSerializer implements INBTHandler<IComputerCubeModule, NBTTagCompound> {
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

    public static class ListSerializer implements INBTSerializer<List<?>, NBTTagList> {
        
        @Override
        public NBTTagList serialize(List<?> value) {
            NBTTagList list = new NBTTagList();

            value.stream()
                    .map(val -> {
                        NBTTagCompound nbt = new NBTTagCompound();
                        NBTSaveHandler.writeClassToNBT(val, nbt);
                        return nbt;
                    })
                    .forEach(list::appendTag);

            return list;
        }
    }
    
    public static abstract class ListDeserializer implements INBTDeserializer<List<?>, NBTTagList> {
        private final BiFunction<Object, Integer, ?> factory;

        public ListDeserializer(BiFunction<Object, Integer, ?> factory) {
            this.factory = factory;
        }

        @Override
        public List<?> deserialize(NBTTagList nbt, Object instance, Class<?> cls) {
            List<Object> list = new ArrayList<>();

            for (int i = 0; i < nbt.tagCount(); i++) {
                NBTTagCompound tag = nbt.getCompoundTagAt(i);
                Object value = this.factory.apply(instance, i);
                NBTSaveHandler.readClassFromNBT(value, tag);
                list.add(value);
            }

            return list;
        }
    }
    
    public static class ListModifyingDeserializer implements INBTModifyingDeserializer<List<?>, NBTTagList> {

        @Override
        public void modifyValue(List<?> value, NBTTagList nbt) {
            if (nbt.getTagType() != 10) {
                throw new IllegalArgumentException("NBTTagList must be of type 10 (NBTTagCompound)");
            }
            else if (value.size() != nbt.tagCount()) {
                GregTechMod.LOGGER.error("Found varying sizes for tag list {} and value {}", nbt, value);
                throw new IllegalArgumentException("Varying sizes of existing and serialized value");
            }

            for (int i = 0; i < value.size(); i++) {
                NBTSaveHandler.readClassFromNBT(value.get(i), nbt.getCompoundTagAt(i));
            }
        }
    }

    public static class ItemStackListNBTSerializer implements INBTHandler<List<ItemStack>, NBTTagList> {
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
    
    static class EnumNBTSerializer implements INBTHandler<Enum<?>, NBTTagString> {
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
    
    static class None implements INBTHandler<Object, NBTBase> {
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
