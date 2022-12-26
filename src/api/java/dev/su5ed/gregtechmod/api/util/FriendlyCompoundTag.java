package dev.su5ed.gregtechmod.api.util;

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

public class FriendlyCompoundTag extends CompoundTag { // TODO interface
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
    public void write(DataOutput pOutput) throws IOException {
        this.wrapped.write(pOutput);
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
    public Tag put(String pKey, Tag pValue) {
        return this.wrapped.put(pKey, pValue);
    }

    @Override
    public void putByte(String pKey, byte pValue) {
        this.wrapped.putByte(pKey, pValue);
    }

    @Override
    public void putShort(String pKey, short pValue) {
        this.wrapped.putShort(pKey, pValue);
    }

    @Override
    public void putInt(String pKey, int pValue) {
        this.wrapped.putInt(pKey, pValue);
    }

    @Override
    public void putLong(String pKey, long pValue) {
        this.wrapped.putLong(pKey, pValue);
    }

    @Override
    public void putUUID(String pKey, UUID pValue) {
        this.wrapped.putUUID(pKey, pValue);
    }

    @Override
    public void putFloat(String pKey, float pValue) {
        this.wrapped.putFloat(pKey, pValue);
    }

    @Override
    public void putDouble(String pKey, double pValue) {
        this.wrapped.putDouble(pKey, pValue);
    }

    @Override
    public void putString(String pKey, String pValue) {
        this.wrapped.putString(pKey, pValue);
    }

    @Override
    public void putByteArray(String pKey, byte[] pValue) {
        this.wrapped.putByteArray(pKey, pValue);
    }

    @Override
    public void putByteArray(String pKey, List<Byte> pValue) {
        this.wrapped.putByteArray(pKey, pValue);
    }

    @Override
    public void putIntArray(String pKey, int[] pValue) {
        this.wrapped.putIntArray(pKey, pValue);
    }

    @Override
    public void putIntArray(String pKey, List<Integer> pValue) {
        this.wrapped.putIntArray(pKey, pValue);
    }

    @Override
    public void putLongArray(String pKey, long[] pValue) {
        this.wrapped.putLongArray(pKey, pValue);
    }

    @Override
    public void putLongArray(String pKey, List<Long> pValue) {
        this.wrapped.putLongArray(pKey, pValue);
    }

    @Override
    public void putBoolean(String pKey, boolean pValue) {
        this.wrapped.putBoolean(pKey, pValue);
    }

    @Nullable
    @Override
    public Tag get(String pKey) {
        return this.wrapped.get(pKey);
    }

    @Override
    public byte getTagType(String pKey) {
        return this.wrapped.getTagType(pKey);
    }

    @Override
    public boolean contains(String pKey) {
        return this.wrapped.contains(pKey);
    }

    @Override
    public boolean contains(String pKey, int pTagType) {
        return this.wrapped.contains(pKey, pTagType);
    }

    @Override
    public byte getByte(String pKey) {
        return this.wrapped.getByte(pKey);
    }

    @Override
    public short getShort(String pKey) {
        return this.wrapped.getShort(pKey);
    }

    @Override
    public int getInt(String pKey) {
        return this.wrapped.getInt(pKey);
    }

    @Override
    public long getLong(String pKey) {
        return this.wrapped.getLong(pKey);
    }

    @Override
    public float getFloat(String pKey) {
        return this.wrapped.getFloat(pKey);
    }

    @Override
    public double getDouble(String pKey) {
        return this.wrapped.getDouble(pKey);
    }

    @Override
    public String getString(String pKey) {
        return this.wrapped.getString(pKey);
    }

    @Override
    public byte[] getByteArray(String pKey) {
        return this.wrapped.getByteArray(pKey);
    }

    @Override
    public int[] getIntArray(String pKey) {
        return this.wrapped.getIntArray(pKey);
    }

    @Override
    public long[] getLongArray(String pKey) {
        return this.wrapped.getLongArray(pKey);
    }

    @Override
    public CompoundTag getCompound(String pKey) {
        return this.wrapped.getCompound(pKey);
    }

    @Override
    public ListTag getList(String pKey, int pTagType) {
        return this.wrapped.getList(pKey, pTagType);
    }

    @Override
    public void remove(String pKey) {
        this.wrapped.remove(pKey);
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
    public boolean equals(Object pOther) {
        return this.wrapped.equals(pOther);
    }

    @Override
    public int hashCode() {
        return this.wrapped.hashCode();
    }

    @Override
    public CompoundTag merge(CompoundTag pOther) {
        return this.wrapped.merge(pOther);
    }

    @Override
    public StreamTagVisitor.ValueResult accept(StreamTagVisitor pVisitor) {
        return this.wrapped.accept(pVisitor);
    }
}
