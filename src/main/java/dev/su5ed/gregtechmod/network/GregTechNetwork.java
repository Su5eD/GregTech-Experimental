package dev.su5ed.gregtechmod.network;

import dev.su5ed.gregtechmod.api.cover.ICover;
import dev.su5ed.gregtechmod.api.cover.ICoverable;
import dev.su5ed.gregtechmod.api.util.NBTTarget;
import dev.su5ed.gregtechmod.blockentity.BaseBlockEntity;
import dev.su5ed.gregtechmod.blockentity.component.BlockEntityComponent;
import dev.su5ed.gregtechmod.util.GtUtil;
import dev.su5ed.gregtechmod.util.nbt.FieldHandle;
import dev.su5ed.gregtechmod.util.nbt.NBTSaveHandler;
import net.minecraft.nbt.CompoundTag;
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
        INSTANCE.registerMessage(id++, BlockEntityUpdate.class, BlockEntityUpdate::encode, BlockEntityUpdate::decode, BlockEntityUpdate::processPacket);
        INSTANCE.registerMessage(id++,
            BlockEntityComponentUpdate.class,
            BlockEntityNamedUpdate::encode,
            buf -> BlockEntityNamedUpdate.decode(buf, BlockEntityComponentUpdate::new),
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
    }

    public static void updateClientField(BlockEntity be, String name) {
        GtUtil.ensureServer(be.getLevel());
        FieldHandle field = NBTSaveHandler.getFieldHandle(name, be);
        CompoundTag tag = NBTSaveHandler.serializeFields(be, NBTTarget.SYNC, field);
        BlockEntityUpdate packet = new BlockEntityUpdate(be, tag);

        sendTrackingChunk(be, packet);
    }

    public static void updateClientComponent(BaseBlockEntity be, BlockEntityComponent component) {
        GtUtil.ensureServer(be.getLevel());
        CompoundTag tag = component.save(NBTTarget.SYNC);
        BlockEntityComponentUpdate packet = new BlockEntityComponentUpdate(be, tag, component.getName());

        sendTrackingChunk(be, packet);
    }

    public static <T extends BlockEntity & ICoverable> void updateClientCover(T be, ICover cover) {
        GtUtil.ensureServer(be.getLevel());
        CompoundTag tag = cover.save(NBTTarget.SYNC);
        BlockEntityCoverUpdate packet = new BlockEntityCoverUpdate(be, tag, cover.getName(), cover.getSide());

        sendTrackingChunk(be, packet);
    }

    private static void sendTrackingChunk(BlockEntity be, Object packet) {
        INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> be.getLevel().getChunkAt(be.getBlockPos())), packet);
    }
}
