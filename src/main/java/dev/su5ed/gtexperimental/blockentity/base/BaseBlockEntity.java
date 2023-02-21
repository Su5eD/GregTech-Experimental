package dev.su5ed.gtexperimental.blockentity.base;

import dev.su5ed.gtexperimental.api.util.FriendlyCompoundTag;
import dev.su5ed.gtexperimental.block.BaseEntityBlock;
import dev.su5ed.gtexperimental.blockentity.component.BlockEntityComponent;
import dev.su5ed.gtexperimental.network.GregTechNetwork;
import dev.su5ed.gtexperimental.util.BlockEntityProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkHooks;
import one.util.streamex.StreamEx;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public abstract class BaseBlockEntity extends BlockEntity {
    protected final BlockEntityProvider.AllowedFacings allowedFacings;
    private final List<BlockEntityComponent> components = new ArrayList<>();

    private boolean isActive;
    private int tickCounter;

    protected BaseBlockEntity(BlockEntityProvider provider, BlockPos pos, BlockState state) {
        super(provider.getType(), pos, state);

        this.allowedFacings = provider.getAllowedFacings();
    }

    public void tickClient() {
    }

    public void tickServer() {
        this.components.forEach(BlockEntityComponent::tickServer);

        this.tickCounter++;
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (this instanceof MenuProvider menuProvider) {
            if (!this.level.isClientSide) {
                NetworkHooks.openScreen((ServerPlayer) player, menuProvider, this.worldPosition);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    public Optional<ItemStack> getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        return Optional.empty();
    }

    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction side) {
        return false;
    }

    public void provideAdditionalDrops(List<? super ItemStack> drops) {}

    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {}

    public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, TooltipFlag flag) {}
    
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return false;
    }
    
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 0;
    }

    public boolean isActive() {
        return this.isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
        setChanged();
        if (!this.level.isClientSide) {
            BlockState state = getBlockState();
            if (state.getValue(BaseEntityBlock.ACTIVE) != active) {
                this.level.setBlockAndUpdate(this.worldPosition, state.setValue(BaseEntityBlock.ACTIVE, active));
            }
        }
    }

    public Direction getFacing() {
        return getBlockState().getValue(BlockStateProperties.FACING);
    }

    public boolean setFacing(Direction side) {
        if (this.allowedFacings.allows(side)) {
            BlockState state = getBlockState();
            this.level.setBlockAndUpdate(this.worldPosition, state.setValue(BlockStateProperties.FACING, side));
            return true;
        }
        return false;
    }

    public Component getDisplayName() {
        return getBlockState().getBlock().getName();
    }

    protected boolean isRedstonePowered() {
        return this.level != null && this.level.hasNeighborSignal(this.worldPosition);
    }

    public int getTicks() {
        return this.tickCounter;
    }

    public boolean wrenchCanRemove() {
        return true;
    }

    public void updateClientField(String name) {
        GregTechNetwork.updateClientField(this, name);
    }

    public void updateNeighbors() {
        BlockState state = getBlockState();
        this.level.sendBlockUpdated(this.worldPosition, state, state, Block.UPDATE_ALL);
    }

    protected <T extends BlockEntityComponent> T addComponent(T component) {
        ResourceLocation name = component.getName();
        if (this.components.stream().map(BlockEntityComponent::getName).anyMatch(name::equals)) {
            throw new RuntimeException("Duplicate component: " + name);
        }
        else {
            this.components.add(component);
            this.components.sort(Comparator.comparingInt(BlockEntityComponent::getPriority).reversed());
        }
        return component;
    }

    public Optional<BlockEntityComponent> getComponent(ResourceLocation name) {
        return StreamEx.of(this.components)
            .findFirst(c -> c.getName().equals(name));
    }

    public Collection<BlockEntityComponent> getComponents() {
        return this.components;
    }

    public void updateRender() {
        if (this.level.isClientSide) {
            requestModelDataUpdate();
        }
        BlockState state = getBlockState();
        this.level.sendBlockUpdated(this.worldPosition, state, state, Block.UPDATE_IMMEDIATE);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (this.level.isClientSide) {
            GregTechNetwork.requestInitialData(this);
        }

        this.components.forEach(BlockEntityComponent::onLoad);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        
        this.components.forEach(BlockEntityComponent::onUnload);
    }

    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return StreamEx.of(this.components)
            .map(component -> component.getCapability(cap, side))
            .findFirst(LazyOptional::isPresent)
            .orElseGet(() -> super.getCapability(cap, side));
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();

        this.components.forEach(BlockEntityComponent::invalidateCaps);
    }

    @Override
    protected final void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        FriendlyCompoundTag friendlyTag = new FriendlyCompoundTag();
        saveAdditional(friendlyTag);
        tag.put("fields", friendlyTag);
        tag.put("components", saveComponents());
    }

    @Override
    public final void load(CompoundTag tag) {
        super.load(tag);

        FriendlyCompoundTag fields = new FriendlyCompoundTag(tag.getCompound("fields"));
        FriendlyCompoundTag components = new FriendlyCompoundTag(tag.getCompound("components"));
        load(fields);
        loadComponents(components);
    }

    protected void saveAdditional(FriendlyCompoundTag tag) {
        tag.putBoolean("isActive", this.isActive);
    }

    protected void load(FriendlyCompoundTag tag) {
        this.isActive = tag.getBoolean("isActive");
    }

    private CompoundTag saveComponents() {
        CompoundTag tag = new CompoundTag();
        this.components.forEach(component -> {
            FriendlyCompoundTag componentTag = component.save();
            tag.put(component.getName().toString(), componentTag);
        });
        return tag;
    }

    private void loadComponents(FriendlyCompoundTag tag) {
        for (BlockEntityComponent component : this.components) {
            String name = component.getName().toString();
            if (tag.contains(name)) {
                component.load(tag.getCompound(name));
            }
        }
    }
}
