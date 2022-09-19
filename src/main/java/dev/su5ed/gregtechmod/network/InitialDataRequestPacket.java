package dev.su5ed.gregtechmod.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record InitialDataRequestPacket(BlockPos pos) {
    
    public static void encode(InitialDataRequestPacket packet, FriendlyByteBuf buf) {
        buf.writeBlockPos(packet.pos);
    }
    
    public static InitialDataRequestPacket decode(FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        return new InitialDataRequestPacket(pos);
    }

    public static void processPacket(InitialDataRequestPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ServerPlayer player = ctx.get().getSender();
        if (player.level.isLoaded(packet.pos)) {
            BlockEntity be = player.level.getBlockEntity(packet.pos);
            if (be != null) {
                FriendlyByteBuf data = NetworkHandler.serializeClass(be);
                BlockEntityUpdate response = new BlockEntityUpdate(be, data);
                GregTechNetwork.INSTANCE.reply(response, ctx.get());
            }
        }
    }
}
