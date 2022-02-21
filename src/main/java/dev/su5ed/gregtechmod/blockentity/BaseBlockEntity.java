package dev.su5ed.gregtechmod.blockentity;

import com.google.common.base.Preconditions;
import dev.su5ed.gregtechmod.blockentity.component.BlockEntityComponent;
import dev.su5ed.gregtechmod.network.GregTechNetwork;
import dev.su5ed.gregtechmod.util.BlockEntityProvider;
import dev.su5ed.gregtechmod.util.nbt.NBTPersistent.Mode;
import dev.su5ed.gregtechmod.util.nbt.NBTSaveHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import one.util.streamex.StreamEx;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class BaseBlockEntity extends BlockEntity {
    private final Map<ResourceLocation, BlockEntityComponent> components = new HashMap<>();

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

    protected <T extends BlockEntityComponent> T addComponent(T component) {
        ResourceLocation name = component.getName();
        if (this.components.containsKey(name)) throw new RuntimeException("Duplicate component: " + name + " of type " + component.getClass().getName());
        else this.components.put(name, component);
        return component;
    }

    public Optional<BlockEntityComponent> getComponent(ResourceLocation name) {
        return Optional.ofNullable(this.components.get(name));
    }

    public void updateModel() {
        Preconditions.checkState(this.level.isClientSide, "Model must only be updated client-side");

        requestModelDataUpdate();
        BlockState state = getBlockState();
        this.level.sendBlockUpdated(this.worldPosition, state, state, Block.UPDATE_IMMEDIATE);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveTag(new CompoundTag(), Mode.SYNC);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        loadTag(tag, true);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        saveTag(tag, Mode.SAVE);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        loadTag(tag, false);
    }

    private CompoundTag saveComponents(Mode mode) {
        CompoundTag tag = new CompoundTag();
        this.components.forEach((name, component) -> {
            CompoundTag compound = component.save(mode);
            tag.put(name.toString(), compound);
        });
        return tag;
    }

    private void loadComponents(CompoundTag tag, boolean notifyListeners) {
        StreamEx.of(tag.getAllKeys())
            .mapToEntry(name -> this.components.get(new ResourceLocation(name)), tag::getCompound)
            .nonNullKeys()
            .forKeyValue((component, compound) -> component.load(compound, notifyListeners));
    }

    private CompoundTag saveTag(CompoundTag tag, Mode mode) {
        tag.put("fields", NBTSaveHandler.writeClassToNBT(this, mode));
        tag.put("components", saveComponents(mode));
        return tag;
    }

    private void loadTag(CompoundTag tag, boolean notifyListeners) {
        NBTSaveHandler.readClassFromNBT(this, tag.getCompound("fields"), notifyListeners);
        loadComponents(tag.getCompound("components"), notifyListeners);
    }
}
