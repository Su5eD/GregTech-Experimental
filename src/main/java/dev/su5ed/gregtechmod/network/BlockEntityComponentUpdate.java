package dev.su5ed.gregtechmod.network;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class BlockEntityComponentUpdate {
    private final BlockPos pos;
    private final ResourceLocation name;
    private final CompoundTag data;

    public BlockEntityComponentUpdate(BlockEntity be, ResourceLocation name, CompoundTag data) {
        this(be.getBlockPos(), name, data);
    }

    public BlockEntityComponentUpdate(BlockPos pos, ResourceLocation name, CompoundTag data) {
        this.pos = pos;
        this.name = name;
        this.data = data;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public ResourceLocation getName() {
        return this.name;
    }

    public CompoundTag getData() {
        return this.data;
    }

    public static void encode(BlockEntityComponentUpdate packet, FriendlyByteBuf buf) {
        buf.writeBlockPos(packet.pos);
        buf.writeResourceLocation(packet.name);
        buf.writeNbt(packet.data);
    }

    public static BlockEntityComponentUpdate decode(FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        ResourceLocation name = buf.readResourceLocation();
        CompoundTag data = buf.readNbt();
        return new BlockEntityComponentUpdate(pos, name, data);
    }

    public static void processPacket(BlockEntityComponentUpdate packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.handleBlockEntityComponentUpdatePacket(packet))
        );
        ctx.get().setPacketHandled(true);
    }
}
