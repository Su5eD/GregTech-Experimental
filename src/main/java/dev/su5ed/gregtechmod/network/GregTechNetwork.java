package dev.su5ed.gregtechmod.network;

import dev.su5ed.gregtechmod.blockentity.BaseBlockEntity;
import dev.su5ed.gregtechmod.blockentity.component.BlockEntityComponent;
import dev.su5ed.gregtechmod.util.nbt.FieldHandle;
import dev.su5ed.gregtechmod.util.nbt.NBTPersistent.Mode;
import dev.su5ed.gregtechmod.util.nbt.NBTSaveHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import static dev.su5ed.gregtechmod.api.util.Reference.location;

public final class GregTechNetwork {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(location("main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    private GregTechNetwork() {}

    public static void registerPackets() {
        int id = 0;
        INSTANCE.registerMessage(id++, BlockEntityUpdate.class, BlockEntityUpdate::encode, BlockEntityUpdate::decode, BlockEntityUpdate::processPacket);
        INSTANCE.registerMessage(id++, BlockEntityComponentUpdate.class, BlockEntityComponentUpdate::encode, BlockEntityComponentUpdate::decode, BlockEntityComponentUpdate::processPacket);
    }

    public static void updateClientField(BlockEntity be, String name) {
        if (!be.getLevel().isClientSide) {
            FieldHandle field = NBTSaveHandler.getFieldHandle(name, be);
            CompoundTag tag = NBTSaveHandler.serializeFields(be, Mode.SYNC, field);
            BlockEntityUpdate packet = new BlockEntityUpdate(be, tag);

            sendTrackingChunk(be, packet);
        }
    }
    
    public static void updateClientComponent(BaseBlockEntity be, BlockEntityComponent component) {
        if (!be.getLevel().isClientSide) {
            CompoundTag tag = NBTSaveHandler.writeClassToNBT(component, Mode.SYNC);
            BlockEntityComponentUpdate packet = new BlockEntityComponentUpdate(be, component.getName(), tag);
            
            sendTrackingChunk(be, packet);
        }
    }
    
    private static void sendTrackingChunk(BlockEntity be, Object packet) {
        INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> be.getLevel().getChunkAt(be.getBlockPos())), packet);
    }
}
