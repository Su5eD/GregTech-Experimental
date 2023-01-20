package dev.su5ed.gtexperimental.network;

import dev.su5ed.gtexperimental.api.cover.Cover;
import dev.su5ed.gtexperimental.blockentity.base.BaseBlockEntity;
import dev.su5ed.gtexperimental.blockentity.component.BlockEntityComponent;
import dev.su5ed.gtexperimental.object.ModCovers;
import dev.su5ed.gtexperimental.util.GtUtil;
import dev.su5ed.gtexperimental.util.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import static dev.su5ed.gtexperimental.api.Reference.location;

public final class GregTechNetwork {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(location("main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    private GregTechNetwork() {}

    public static void registerPackets() {
        int id = 0;
        INSTANCE.messageBuilder(BlockEntityUpdate.class, id++, NetworkDirection.PLAY_TO_CLIENT)
            .encoder(BlockEntityUpdate::encode)
            .decoder(BlockEntityUpdate::decode)
            .consumerMainThread(BlockEntityUpdate::processPacket)
            .add();
        INSTANCE.messageBuilder(BlockEntityComponentUpdate.class, id++, NetworkDirection.PLAY_TO_CLIENT)
            .encoder(BlockEntityComponentUpdate::encode)
            .decoder(buf -> BlockEntityComponentUpdate.decode(buf, BlockEntityComponentUpdate::new))
            .consumerMainThread(BlockEntityComponentUpdate::processPacket)
            .add();
        INSTANCE.messageBuilder(BlockEntityCoverUpdate.class, id++, NetworkDirection.PLAY_TO_CLIENT)
            .encoder(BlockEntityCoverUpdate::encode)
            .decoder(BlockEntityCoverUpdate::decode)
            .consumerMainThread(BlockEntityCoverUpdate::processPacket)
            .add();
        INSTANCE.messageBuilder(JumpChargeUpdate.class, id++, NetworkDirection.PLAY_TO_CLIENT)
            .encoder(JumpChargeUpdate::encode)
            .decoder(JumpChargeUpdate::decode)
            .consumerMainThread(JumpChargeUpdate::processPacket)
            .add();

        INSTANCE.messageBuilder(InitialDataRequestPacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
            .encoder(InitialDataRequestPacket::encode)
            .decoder(InitialDataRequestPacket::decode)
            .consumerMainThread(InitialDataRequestPacket::processPacket)
            .add();
        INSTANCE.messageBuilder(KeyPressUpdate.class, id++, NetworkDirection.PLAY_TO_SERVER)
            .encoder(KeyPressUpdate::encode)
            .decoder(KeyPressUpdate::decode)
            .consumerMainThread(KeyPressUpdate::processPacket)
            .add();
        INSTANCE.messageBuilder(SlotScrollPacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
            .encoder(SlotScrollPacket::encode)
            .decoder(SlotScrollPacket::decode)
            .consumerMainThread(SlotScrollPacket::processPacket)
            .add();
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

    public static void updateKeyPress(KeyPressUpdate.Action action, KeyboardHandler.Key key) {
        if (Minecraft.getInstance().getConnection() != null) {
            KeyPressUpdate packet = new KeyPressUpdate(action, key);
            INSTANCE.sendToServer(packet);
        }
    }

    public static void sendTrackingChunk(BlockEntity be, Object packet) {
        INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> be.getLevel().getChunkAt(be.getBlockPos())), packet);
    }

    public static void sendToPlayer(ServerPlayer player, Object packet) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }
}
