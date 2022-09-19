package dev.su5ed.gregtechmod.network;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record BlockEntityCoverUpdate(BlockPos pos, ResourceLocation name, Direction side, FriendlyByteBuf data) {

    public BlockEntityCoverUpdate(BlockEntity be, ResourceLocation name, Direction side, FriendlyByteBuf data) {
        this(be.getBlockPos(), name, side, data);
    }

    public static void encode(BlockEntityCoverUpdate packet, FriendlyByteBuf buf) {
        buf.writeBlockPos(packet.pos);
        buf.writeResourceLocation(packet.name);
        buf.writeEnum(packet.side);
        buf.writeInt(packet.data.readableBytes());
        buf.writeBytes(packet.data);
    }

    public static BlockEntityCoverUpdate decode(FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        ResourceLocation name = buf.readResourceLocation();
        Direction side = buf.readEnum(Direction.class);
        int length = buf.readInt();
        FriendlyByteBuf data = new FriendlyByteBuf(buf.readBytes(length));

        return new BlockEntityCoverUpdate(pos, name, side, data);
    }

    public static void processPacket(BlockEntityCoverUpdate packet, Supplier<NetworkEvent.Context> ctx) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.handleBlockEntityCoverUpdatePacket(packet));
    }
}
