package mods.gregtechmod.world;

import ic2.api.network.IGrowingBuffer;
import ic2.api.network.INetworkCustomEncoder;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import mods.gregtechmod.api.util.Reference;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class IDSUData extends WorldSavedData {
    public static final String DATA_NAME = Reference.MODID + "_idsu_data";

    private final Map<UUID, EnergyWrapper> data = new Object2ObjectOpenHashMap<>();

    public IDSUData() {
        this(DATA_NAME);
    }

    // Required
    public IDSUData(String name) {
        super(name);
    }

    public static IDSUData get(World world) {
        MapStorage storage = Objects.requireNonNull(world.getMapStorage());

        return Optional.ofNullable((IDSUData) storage.getOrLoadData(IDSUData.class, DATA_NAME))
            .orElseGet(() -> {
                IDSUData data = new IDSUData();
                storage.setData(DATA_NAME, data);
                return data;
            });
    }

    public EnergyWrapper getOrCreateWrapper(UUID uuid) {
        return this.data.computeIfAbsent(uuid, u -> new EnergyWrapper(this));
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        NBTTagList list = nbt.getTagList(DATA_NAME, 10);
        list.forEach(tag -> {
            UUID uuid = UUID.fromString(((NBTTagCompound) tag).getString("uuid"));
            getOrCreateWrapper(uuid).energy = ((NBTTagCompound) tag).getDouble("energy");
        });
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTTagList list = new NBTTagList();
        this.data.forEach((uuid, wrapper) -> {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("uuid", uuid.toString());
            tag.setDouble("energy", wrapper.energy);
            list.appendTag(tag);
        });
        nbt.setTag(DATA_NAME, list);
        return nbt;
    }

    public static class EnergyWrapper {
        public static final EnergyWrapper EMPTY = new EnergyWrapper(null);
        public static final int CAPACITY = 1000000000;

        private final IDSUData parent;
        private double energy;

        private EnergyWrapper(IDSUData parent) {
            this.parent = parent;
        }

        public double getEnergy() {
            return this.energy;
        }

        public double addEnergy(double energy) {
            double injected = Math.min(CAPACITY - this.energy, energy);
            this.energy += injected;
            if (this.parent != null) this.parent.markDirty();
            return injected;
        }

        public double removeEnergy(double energy, boolean simulate) {
            if (this.energy >= energy) {
                if (!simulate) this.energy -= energy;
                if (this.parent != null) this.parent.markDirty();
                return energy;
            }
            return 0;
        }

        public static class EnergyWrapperEncoder implements INetworkCustomEncoder {

            @Override
            public void encode(IGrowingBuffer buffer, Object o) throws IOException {
                buffer.writeDouble(((EnergyWrapper) o).getEnergy());
            }

            @Override
            public Object decode(IGrowingBuffer buffer) throws IOException {
                double energy = buffer.readDouble();
                EnergyWrapper wrapper = new EnergyWrapper(null);
                wrapper.energy = energy;
                return wrapper;
            }

            @Override
            public boolean isThreadSafe() {
                return true;
            }
        }
    }
}
