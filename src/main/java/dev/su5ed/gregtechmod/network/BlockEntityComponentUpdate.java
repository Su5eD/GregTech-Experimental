package dev.su5ed.gregtechmod.network;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class BlockEntityComponentUpdate extends BlockEntityNamedUpdate {

    public BlockEntityComponentUpdate(BlockEntity be, CompoundTag data, ResourceLocation name) {
        super(be, data, name);
    }

    public BlockEntityComponentUpdate(BlockPos pos, CompoundTag data, ResourceLocation name) {
        super(pos, data, name);
    }

    public static void processPacket(BlockEntityComponentUpdate packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.handleBlockEntityComponentUpdatePacket(packet))
        );
        ctx.get().setPacketHandled(true);
    }
}
