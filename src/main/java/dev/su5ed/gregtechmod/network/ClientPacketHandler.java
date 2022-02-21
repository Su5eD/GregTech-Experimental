package dev.su5ed.gregtechmod.network;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.entity.BlockEntity;

public final class ClientPacketHandler {

    private ClientPacketHandler() {}

    public static void handleBlockEntityUpdatePacket(BlockEntityUpdate packet) {
        Minecraft mc = Minecraft.getInstance();
        mc.doRunTask(() -> {
            BlockEntity be = mc.level.getBlockEntity(packet.getPos());
            if (be != null) GregTechNetwork.receiveFieldUpdate(be, packet.getData());
        });
    }
}
