package dev.su5ed.gregtechmod.network;

import dev.su5ed.gregtechmod.api.util.TriFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class BlockEntityNamedUpdate extends BlockEntityUpdate {
    private final ResourceLocation name;

    protected BlockEntityNamedUpdate(BlockEntity be, CompoundTag data, ResourceLocation name) {
        this(be.getBlockPos(), data, name);
    }

    protected BlockEntityNamedUpdate(BlockPos pos, CompoundTag data, ResourceLocation name) {
        super(pos, data);

        this.name = name;
    }

    public ResourceLocation getName() {
        return this.name;
    }

    public static void encode(BlockEntityNamedUpdate packet, FriendlyByteBuf buf) {
        BlockEntityUpdate.encode(packet, buf);
        buf.writeResourceLocation(packet.name);
    }

    public static <T> T decode(FriendlyByteBuf buf, TriFunction<BlockPos, CompoundTag, ResourceLocation, T> factory) {
        BlockPos pos = buf.readBlockPos();
        CompoundTag data = buf.readNbt();
        ResourceLocation name = buf.readResourceLocation();
        
        return factory.apply(pos, data, name);
    }
}
