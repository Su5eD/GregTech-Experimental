package dev.su5ed.gregtechmod.network;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class BlockEntityUpdate {
    private final BlockPos pos;
    private final CompoundTag data;

    public BlockEntityUpdate(BlockEntity be, CompoundTag data) {
        this(be.getBlockPos(), data);
    }

    public BlockEntityUpdate(BlockPos pos, CompoundTag data) {
        this.pos = pos;
        this.data = data;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public CompoundTag getData() {
        return this.data;
    }

    public static void encode(BlockEntityUpdate packet, FriendlyByteBuf buf) {
        buf.writeBlockPos(packet.pos);
        buf.writeNbt(packet.data);
    }

    public static BlockEntityUpdate decode(FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        CompoundTag data = buf.readNbt();
        return new BlockEntityUpdate(pos, data);
    }

    public static void processPacket(BlockEntityUpdate packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.handleBlockEntityUpdatePacket(packet))
        );
        ctx.get().setPacketHandled(true);
    }
}
