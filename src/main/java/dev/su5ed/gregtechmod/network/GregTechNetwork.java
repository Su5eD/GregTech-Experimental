package dev.su5ed.gregtechmod.network;

import dev.su5ed.gregtechmod.api.cover.Cover;
import dev.su5ed.gregtechmod.blockentity.base.BaseBlockEntity;
import dev.su5ed.gregtechmod.blockentity.component.BlockEntityComponent;
import dev.su5ed.gregtechmod.object.ModCovers;
import dev.su5ed.gregtechmod.util.GtUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

import static dev.su5ed.gregtechmod.api.util.Reference.location;

public final class GregTechNetwork {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(location("main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    private GregTechNetwork() {}

    public static void registerPackets() {
        int id = 0;
        INSTANCE.registerMessage(id++,
            BlockEntityUpdate.class,
            BlockEntityUpdate::encode,
            BlockEntityUpdate::decode,
            BlockEntityUpdate::processPacket,
            Optional.of(NetworkDirection.PLAY_TO_CLIENT)
        );
        INSTANCE.registerMessage(id++,
            BlockEntityComponentUpdate.class,
            BlockEntityComponentUpdate::encode,
            buf -> BlockEntityComponentUpdate.decode(buf, BlockEntityComponentUpdate::new),
            BlockEntityComponentUpdate::processPacket,
            Optional.of(NetworkDirection.PLAY_TO_CLIENT)
        );
        INSTANCE.registerMessage(id++,
            BlockEntityCoverUpdate.class,
            BlockEntityCoverUpdate::encode,
            BlockEntityCoverUpdate::decode,
            BlockEntityCoverUpdate::processPacket,
            Optional.of(NetworkDirection.PLAY_TO_CLIENT)
        );
        
        INSTANCE.registerMessage(id++,
            InitialDataRequestPacket.class,
            InitialDataRequestPacket::encode,
            InitialDataRequestPacket::decode,
            InitialDataRequestPacket::processPacket,
            Optional.of(NetworkDirection.PLAY_TO_SERVER)
        );
    }
    
    public static void requestInitialData(BlockEntity be) {
        GtUtil.assertClientSide(be.getLevel());
        InitialDataRequestPacket packet = new InitialDataRequestPacket(be.getBlockPos());
        
        INSTANCE.sendToServer(packet);
    }

    public static void updateClientField(BlockEntity be, String name) {
        GtUtil.assertServerSide(be.getLevel());
        FriendlyByteBuf data = NetworkHandler.serializeField(be, name);
        BlockEntityUpdate packet = new BlockEntityUpdate(be, data);

        sendTrackingChunk(be, packet);
    }

    public static void updateClientComponent(BaseBlockEntity be, BlockEntityComponent component) {
        GtUtil.assertServerSide(be.getLevel());
        FriendlyByteBuf data = NetworkHandler.serializeClass(component);
        BlockEntityComponentUpdate packet = new BlockEntityComponentUpdate(be, data, component.getName());

        sendTrackingChunk(be, packet);
    }

    public static void updateClientCover(BlockEntity be, Cover<?> cover) {
        GtUtil.assertServerSide(be.getLevel());
        FriendlyByteBuf tag = NetworkHandler.serializeClass(cover);
        BlockEntityCoverUpdate packet = new BlockEntityCoverUpdate(be, ModCovers.REGISTRY.get().getKey(cover.getType()), cover.getSide(), tag);

        sendTrackingChunk(be, packet);
    }

    public static void sendTrackingChunk(BlockEntity be, Object packet) {
        INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> be.getLevel().getChunkAt(be.getBlockPos())), packet);
    }
}
