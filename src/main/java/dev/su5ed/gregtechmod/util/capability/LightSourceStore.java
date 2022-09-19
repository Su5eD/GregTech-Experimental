package dev.su5ed.gregtechmod.util.capability;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import org.jetbrains.annotations.Nullable;

public class LightSourceStore implements LightSource {
    private BlockPos pos;

    @Nullable
    @Override
    public BlockPos getSourcePos() {
        return this.pos;
    }

    @Override
    public void setSourcePos(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        if (this.pos != null) {
            tag.put("pos", NbtUtils.writeBlockPos(this.pos));
        }
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.pos = nbt.contains("pos") ? NbtUtils.readBlockPos(nbt.getCompound("pos")) : null;
    }
}
