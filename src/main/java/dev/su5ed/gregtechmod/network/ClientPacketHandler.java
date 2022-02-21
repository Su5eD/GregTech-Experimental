package dev.su5ed.gregtechmod.network;

import dev.su5ed.gregtechmod.blockentity.BaseBlockEntity;
import dev.su5ed.gregtechmod.util.nbt.NBTSaveHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.function.Consumer;

public final class ClientPacketHandler {

    private ClientPacketHandler() {}

    public static void handleBlockEntityUpdatePacket(BlockEntityUpdate packet) {
        runBeTask(packet.getPos(), be -> NBTSaveHandler.readClassFromNBT(be, packet.getData()));
    }

    public static void handleBlockEntityComponentUpdatePacket(BlockEntityComponentUpdate packet) {
        runBeTask(packet.getPos(), be -> {
            if (be instanceof BaseBlockEntity base) {
                base.getComponent(packet.getName()).ifPresent(component -> NBTSaveHandler.readClassFromNBT(component, packet.getData()));
            }
        });
    }

    private static void runBeTask(BlockPos pos, Consumer<BlockEntity> consumer) {
        Minecraft mc = Minecraft.getInstance();
        mc.doRunTask(() -> {
            BlockEntity be = mc.level.getBlockEntity(pos);
            if (be != null) consumer.accept(be);
        });
    }
}
