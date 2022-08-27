package dev.su5ed.gregtechmod.network;

import dev.su5ed.gregtechmod.Capabilities;
import dev.su5ed.gregtechmod.blockentity.base.BaseBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.function.Consumer;

public final class ClientPacketHandler {

    private ClientPacketHandler() {}

    public static void handleBlockEntityUpdatePacket(BlockEntityUpdate packet) {
        runBlockEntityTask(packet.getPos(), be -> NetworkHandler.deserializeClass(packet.getData(), be));
    }

    public static void handleBlockEntityComponentUpdatePacket(BlockEntityComponentUpdate packet) {
        runBlockEntityTask(packet.getPos(), be -> {
            if (be instanceof BaseBlockEntity base) {
                base.getComponent(packet.getName())
                    .ifPresent(component -> NetworkHandler.deserializeClass(packet.getData(), component));
            }
        });
    }

    public static void handleBlockEntityCoverUpdatePacket(BlockEntityCoverUpdate packet) {
        runBlockEntityTask(packet.pos(), be -> {
            be.getCapability(Capabilities.COVERABLE).resolve()
                .flatMap(handler -> handler.getCoverAtSide(packet.side()))
                .filter(cover -> cover.getType().getRegistryName().equals(packet.name()))
                .ifPresent(cover -> NetworkHandler.deserializeClass(packet.data(), cover));
        });
    }

    private static void runBlockEntityTask(BlockPos pos, Consumer<BlockEntity> consumer) {
        Minecraft mc = Minecraft.getInstance();
        mc.doRunTask(() -> {
            if (mc.level.isLoaded(pos)) {
                BlockEntity be = mc.level.getBlockEntity(pos);
                if (be != null) consumer.accept(be);
            }
        });
    }
}
