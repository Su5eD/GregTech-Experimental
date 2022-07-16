package dev.su5ed.gregtechmod.network;

import dev.su5ed.gregtechmod.api.cover.Coverable;
import dev.su5ed.gregtechmod.blockentity.base.BaseBlockEntity;
import dev.su5ed.gregtechmod.util.nbt.NBTSaveHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.function.Consumer;

public final class ClientPacketHandler {

    private ClientPacketHandler() {}

    public static void handleBlockEntityUpdatePacket(BlockEntityUpdate packet) {
        runBlockEntityTask(packet.getPos(), be -> NBTSaveHandler.readClassFromNBT(be, packet.getData()));
    }

    public static void handleBlockEntityComponentUpdatePacket(BlockEntityComponentUpdate packet) {
        runBlockEntityTask(packet.getPos(), be -> {
            if (be instanceof BaseBlockEntity base) {
                base.getComponent(packet.getName()).ifPresent(component -> component.load(packet.getData(), true));
            }
        });
    }

    public static void handleBlockEntityCoverUpdatePacket(BlockEntityCoverUpdate packet) {
        runBlockEntityTask(packet.getPos(), be -> {
            if (be instanceof Coverable coverable) {
                coverable.getCoverAtSide(packet.getSide())
                    .filter(cover -> cover.getType().getRegistryName().equals(packet.getName()))
                    .ifPresent(cover -> cover.load(packet.getData(), true));
            }
        });
    }

    private static void runBlockEntityTask(BlockPos pos, Consumer<BlockEntity> consumer) {
        Minecraft mc = Minecraft.getInstance();
        mc.doRunTask(() -> {
            BlockEntity be = mc.level.getBlockEntity(pos);
            if (be != null) consumer.accept(be);
        });
    }
}
