package dev.su5ed.gregtechmod.util.nbt;

import com.mojang.authlib.GameProfile;
import dev.su5ed.gregtechmod.GregTechMod;
import dev.su5ed.gregtechmod.api.util.NBTTarget;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import one.util.streamex.StreamEx;

import java.util.List;

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

    public static class ListSerializer implements NBTSerializer<List<?>, ListTag> {
        public static final ListSerializer INSTANCE = new ListSerializer();

        @Override
        public ListTag serialize(List<?> value, NBTTarget target) {
            return StreamEx.of(value)
                .<Tag>map(NBTSaveHandler::writeClassToNBT)
                .toCollection(ListTag::new);
        }
    }

    public static class ListModifyingDeserializer implements NBTModifyingDeserializer<List<?>, ListTag> {
        public static final ListModifyingDeserializer INSTANCE = new ListModifyingDeserializer();

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
        public static final ItemStackListNBTSerializer INSTANCE = new ItemStackListNBTSerializer();

        @Override
        public ListTag serialize(List<ItemStack> value, NBTTarget target) {
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
        public static final EnumNBTSerializer INSTANCE = new EnumNBTSerializer();

        @Override
        public StringTag serialize(Enum<?> value, NBTTarget target) {
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
        public Tag serialize(Object value, NBTTarget target) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object deserialize(Tag tag, Object instance, Class<?> cls) {
            throw new UnsupportedOperationException();
        }
    }
}
