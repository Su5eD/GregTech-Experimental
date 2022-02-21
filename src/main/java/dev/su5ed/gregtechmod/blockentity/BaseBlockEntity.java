package dev.su5ed.gregtechmod.blockentity;

import dev.su5ed.gregtechmod.network.GregTechNetwork;
import dev.su5ed.gregtechmod.util.BlockEntityProvider;
import dev.su5ed.gregtechmod.util.nbt.NBTPersistent.Mode;
import dev.su5ed.gregtechmod.util.nbt.NBTSaveHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;

import java.util.Optional;

public abstract class BaseBlockEntity extends BlockEntity {
    
    protected BaseBlockEntity(BlockEntityProvider provider, BlockPos pos, BlockState state) {
        this(provider.getType(), pos, state);
    }

    protected BaseBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    
    public void tickClient() {
    }
    
    public void tickServer() {
    }
    
    public Optional<ItemStack> getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        return Optional.empty();
    }
    
    public void updateClientField(String name) {
        GregTechNetwork.updateClientField(this, name);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return NBTSaveHandler.writeClassToNBT(this, Mode.SYNC);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        NBTSaveHandler.readClassFromNBT(this, tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        
        CompoundTag compound = tag.getCompound("fields");
        NBTSaveHandler.readClassFromNBT(this, compound);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        
        CompoundTag compound = NBTSaveHandler.writeClassToNBT(this);
        tag.put("fields", compound);
    }
}
