package dev.su5ed.gtexperimental.network;

import dev.su5ed.gtexperimental.util.TriFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class BlockEntityNamedUpdate extends BlockEntityUpdate {
    private final ResourceLocation name;

    protected BlockEntityNamedUpdate(BlockEntity be, FriendlyByteBuf data, ResourceLocation name) {
        this(be.getBlockPos(), data, name);
    }

    protected BlockEntityNamedUpdate(BlockPos pos, FriendlyByteBuf data, ResourceLocation name) {
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

    public static <T> T decode(FriendlyByteBuf buf, TriFunction<BlockPos, FriendlyByteBuf, ResourceLocation, T> factory) {
        BlockPos pos = buf.readBlockPos();
        int size = buf.readInt();
        FriendlyByteBuf data = new FriendlyByteBuf(buf.readBytes(size));
        ResourceLocation name = buf.readResourceLocation();
        
        return factory.apply(pos, data, name);
    }
}
