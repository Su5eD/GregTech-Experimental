package dev.su5ed.gtexperimental.network;

import dev.su5ed.gtexperimental.blockentity.component.BlockEntityComponent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import one.util.streamex.StreamEx;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class SynchronizedData {
    private final Map<Key, Value> syncedValues;

    public SynchronizedData(Set<Key> keys) {
        this.syncedValues = StreamEx.of(keys)
            .mapToEntry(Value::new)
            .toMap();
    }

    public void sync(ServerPlayer player, BlockEntity be) {
        this.syncedValues.forEach((key, value) -> {
            if (value.isChanged(be)) {
                Object packet = key.createPacket(be);
                GregTechNetwork.sendToPlayer(player, packet);
            }
        });
    }

    public static class Key {
        private final Function<BlockEntity, ?> getter;
        private final Function<BlockEntity, ?> packetSupplier;

        public static Key component(BlockEntityComponent component, String field) {
            return new Key(
                be -> NetworkHandler.getFieldValue(component, field),
                be -> {
                    FriendlyByteBuf data = NetworkHandler.serializeField(component, field);
                    return new BlockEntityComponentUpdate(be, data, component.getName());
                }
            );
        }

        public static Key field(String field) {
            return new Key(be -> NetworkHandler.getFieldValue(be, field), be -> {
                FriendlyByteBuf data = NetworkHandler.serializeField(be, field);
                return new BlockEntityUpdate(be, data);
            });
        }

        private Key(Function<BlockEntity, ?> getter, Function<BlockEntity, ?> packetSupplier) {
            this.getter = getter;
            this.packetSupplier = packetSupplier;
        }

        public Object getValue(BlockEntity be) {
            return this.getter.apply(be);
        }

        public Object createPacket(BlockEntity be) {
            return this.packetSupplier.apply(be);
        }
    }

    public static class Value {
        private final Key key;

        private int previousValueHashCode;

        public Value(Key key) {
            this.key = key;
        }

        public boolean isChanged(BlockEntity be) {
            Object value = this.key.getValue(be);
            int hashCode = value == null ? 0 : value.hashCode();
            if (this.previousValueHashCode != hashCode) {
                this.previousValueHashCode = hashCode;
                return true;
            }
            return false;
        }
    }
}
