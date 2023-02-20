package dev.su5ed.gtexperimental.api.util;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.StreamTagVisitor;
import net.minecraft.nbt.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.io.DataOutput;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

public class FriendlyCompoundTag extends CompoundTag {
    public static final Codec<FriendlyCompoundTag> CODEC = CompoundTag.CODEC.xmap(FriendlyCompoundTag::new, Function.identity());
    
    private static final Logger LOGGER = LogManager.getLogger();
    
    private final CompoundTag wrapped;

    public FriendlyCompoundTag() {
        this(new CompoundTag());
    }

    public FriendlyCompoundTag(CompoundTag wrapped) {
        this.wrapped = wrapped;
    }

    public <T> void put(String name, T value, Codec<T> codec) {
        Tag tag = codec.encodeStart(NbtOps.INSTANCE, value)
            .getOrThrow(false, s -> {});
        put(name, tag);
    }
    
    public <T> T get(String name, Codec<T> codec) {
        Tag tag = get(name);
        return codec.decode(NbtOps.INSTANCE, tag)
            .getOrThrow(false, s -> {})
            .getFirst();
    }
    
    public <T extends Enum<T>> void putEnum(String name, T value) {
        CompoundTag tag = new CompoundTag();
        tag.putString("name", value.name());
        tag.putString("type", value.getClass().getName());
        put(name, tag);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public <T extends Enum<T>> T getEnum(String name) {
        CompoundTag tag = getCompound(name);
        String enumName = tag.getString("name");
        String typeName = tag.getString("type");
        try {
            Class type = Class.forName(typeName);
            return (T) Enum.valueOf(type, enumName);
        } catch (ClassNotFoundException e) {
            LOGGER.error("Failed to deserialize enum", e);
        }
        return null;
    }

    @Override
    public void write(DataOutput output) throws IOException {
        this.wrapped.write(output);
    }

    @Override
    public Set<String> getAllKeys() {
        return this.wrapped.getAllKeys();
    }

    @Override
    public int size() {
        return this.wrapped.size();
    }

    @Nullable
    @Override
    public Tag put(String key, Tag value) {
        return this.wrapped.put(key, value);
    }

    @Override
    public void putByte(String key, byte value) {
        this.wrapped.putByte(key, value);
    }

    @Override
    public void putShort(String key, short value) {
        this.wrapped.putShort(key, value);
    }

    @Override
    public void putInt(String key, int value) {
        this.wrapped.putInt(key, value);
    }

    @Override
    public void putLong(String key, long value) {
        this.wrapped.putLong(key, value);
    }

    @Override
    public void putUUID(String key, UUID value) {
        this.wrapped.putUUID(key, value);
    }

    @Override
    public void putFloat(String key, float value) {
        this.wrapped.putFloat(key, value);
    }

    @Override
    public void putDouble(String key, double value) {
        this.wrapped.putDouble(key, value);
    }

    @Override
    public void putString(String key, String value) {
        this.wrapped.putString(key, value);
    }

    @Override
    public void putByteArray(String key, byte[] value) {
        this.wrapped.putByteArray(key, value);
    }

    @Override
    public void putByteArray(String key, List<Byte> value) {
        this.wrapped.putByteArray(key, value);
    }

    @Override
    public void putIntArray(String key, int[] value) {
        this.wrapped.putIntArray(key, value);
    }

    @Override
    public void putIntArray(String key, List<Integer> value) {
        this.wrapped.putIntArray(key, value);
    }

    @Override
    public void putLongArray(String key, long[] value) {
        this.wrapped.putLongArray(key, value);
    }

    @Override
    public void putLongArray(String key, List<Long> value) {
        this.wrapped.putLongArray(key, value);
    }

    @Override
    public void putBoolean(String key, boolean value) {
        this.wrapped.putBoolean(key, value);
    }

    @Nullable
    @Override
    public Tag get(String key) {
        return this.wrapped.get(key);
    }

    @Override
    public byte getTagType(String key) {
        return this.wrapped.getTagType(key);
    }

    @Override
    public boolean contains(String key) {
        return this.wrapped.contains(key);
    }

    @Override
    public boolean contains(String key, int tagType) {
        return this.wrapped.contains(key, tagType);
    }

    @Override
    public byte getByte(String key) {
        return this.wrapped.getByte(key);
    }

    @Override
    public short getShort(String key) {
        return this.wrapped.getShort(key);
    }

    @Override
    public int getInt(String key) {
        return this.wrapped.getInt(key);
    }

    @Override
    public long getLong(String key) {
        return this.wrapped.getLong(key);
    }

    @Override
    public float getFloat(String key) {
        return this.wrapped.getFloat(key);
    }

    @Override
    public double getDouble(String key) {
        return this.wrapped.getDouble(key);
    }

    @Override
    public String getString(String key) {
        return this.wrapped.getString(key);
    }

    @Override
    public byte[] getByteArray(String key) {
        return this.wrapped.getByteArray(key);
    }

    @Override
    public int[] getIntArray(String key) {
        return this.wrapped.getIntArray(key);
    }

    @Override
    public long[] getLongArray(String key) {
        return this.wrapped.getLongArray(key);
    }

    @Override
    public CompoundTag getCompound(String key) {
        return this.wrapped.getCompound(key);
    }

    @Override
    public ListTag getList(String key, int tagType) {
        return this.wrapped.getList(key, tagType);
    }

    @Override
    public void remove(String key) {
        this.wrapped.remove(key);
    }

    @Override
    public boolean isEmpty() {
        return this.wrapped.isEmpty();
    }

    @Override
    public CompoundTag copy() {
        return this.wrapped.copy();
    }

    @Override
    public boolean equals(Object other) {
        return this.wrapped.equals(other);
    }

    @Override
    public int hashCode() {
        return this.wrapped.hashCode();
    }

    @Override
    public CompoundTag merge(CompoundTag other) {
        return this.wrapped.merge(other);
    }

    @Override
    public StreamTagVisitor.ValueResult accept(StreamTagVisitor visitor) {
        return this.wrapped.accept(visitor);
    }
}
