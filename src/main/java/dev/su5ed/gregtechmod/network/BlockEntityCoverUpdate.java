package dev.su5ed.gregtechmod.network;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class BlockEntityCoverUpdate extends BlockEntityNamedUpdate {
    private final Direction side;

    public BlockEntityCoverUpdate(BlockEntity be, CompoundTag data, ResourceLocation name, Direction side) {
        this(be.getBlockPos(), data, name, side);
    }

    public BlockEntityCoverUpdate(BlockPos pos, CompoundTag data, ResourceLocation name, Direction side) {
        super(pos, data, name);

        this.side = side;
    }

    public Direction getSide() {
        return this.side;
    }
    
    public static void encode(BlockEntityCoverUpdate packet, FriendlyByteBuf buf) {
        BlockEntityNamedUpdate.encode(packet, buf);
        buf.writeEnum(packet.side);
    }

    public static BlockEntityCoverUpdate decode(FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        CompoundTag data = buf.readNbt();
        ResourceLocation name = buf.readResourceLocation();
        Direction side = buf.readEnum(Direction.class);

        return new BlockEntityCoverUpdate(pos, data, name, side);
    }

    public static void processPacket(BlockEntityCoverUpdate packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.handleBlockEntityCoverUpdatePacket(packet))
        );
        ctx.get().setPacketHandled(true);
    }
}
